package csi.attendence.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import csi.attendence.entity.EmailVerification;
import csi.attendence.entity.TokenValidations;
import csi.attendence.entity.User;
import csi.attendence.exceptions.BadRequestException;
import csi.attendence.exceptions.EmailVerificationExpiredException;
import csi.attendence.repository.EmailRepository;
import csi.attendence.repository.TokenValidationsRepository;
import csi.attendence.utils.UrlUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmailServiceImpl {

	public EmailServiceImpl(EmailRepository emailRepository, TokenValidationsRepository tokenValidationsRepository,
			JavaMailSender mailSender) {
		super();
		this.emailRepository = emailRepository;
		this.tokenValidationsRepository = tokenValidationsRepository;
		this.mailSender = mailSender;
	}

	@Value("${spring.mail.username}")
	private String sender;

	@Value("${spring.application.name}")
	private String companyName;

	private final EmailRepository emailRepository;
	

	private final TokenValidationsRepository tokenValidationsRepository;

	private final JavaMailSender mailSender;

	@Value("${application.email_verification.expires}")
	private Integer expireMinutes;

	public void passwordReset(User user, HttpServletRequest httpServletRequest) {

		TokenValidations tokenValidations = new TokenValidations();
		tokenValidations.setActive(true);
		tokenValidations.setToken(buildToken());
		tokenValidations.setUser(user);
		tokenValidations.setTokenExpires(calculateExpiryDate(60));
		this.tokenValidationsRepository.deleteByUser(user);
		this.tokenValidationsRepository.save(tokenValidations);

		try {
			sendPasswordResetEmail(user, UrlUtils.getSiteURL(httpServletRequest), tokenValidations.getToken());
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
	}

	public void validateResetPasswordVerificationCode(String code) {
		if (Strings.isBlank(code)) {
			throw new BadRequestException("Valid code is required");
		}
		TokenValidations tokenValidations = this.tokenValidationsRepository.findByTokenAndActive(code,true)
				.orElseThrow(() -> new RuntimeException());

		if (tokenValidations.getTokenExpires().before(new Date())) {
			throw new RuntimeException("token is expired");
		}
		this.tokenValidationsRepository.save(tokenValidations);
	}

	public void validateEmailVerificationCode(String code) {
		if (Strings.isBlank(code)) {
			throw new BadRequestException("Valid code is required");
		}

		EmailVerification emailVerification = this.emailRepository.findByToken(code)
				.orElseThrow(() -> new EmailVerificationExpiredException());
		if (emailVerification.getExpiryDate().before(new Date())) {
			throw new RuntimeException("token is expired");
		}
		emailVerification.setActive(false);
		emailVerification.setEmailVerified(true);
		
		this.emailRepository.save(emailVerification);
	}

	public void sendVerificationEmailToUser(User user, String siteURL) {
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			return;
		}
		EmailVerification emailVerification = new EmailVerification();

		emailVerification.setActive(true);
		emailVerification.setEmail(user.getEmail());
		emailVerification.setToken(buildToken());
		emailVerification.setEmailVerified(false);
		emailVerification.setExpiryDate(emailVerification.calculateExpiryDate(this.expireMinutes));
		emailVerification.setUser(user);
		this.emailRepository.save(emailVerification);
		try {
			sendVerificationEmail(user, siteURL, emailVerification.getToken());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

	private static String buildToken() {
		String randomString = UUID.randomUUID().toString();
//		String time = "" + new Date().getTime();

//		String randomTime = Base64.getEncoder().encode(time.getBytes()).toString();
		return randomString;
	}

	private void sendPasswordResetEmail(User user, String siteURL, String verificationCode)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = sender;
		String senderName = companyName;
		String subject = "Password Reset Email for Qr Based Attendence Management System";
		String content = emailTemplateForVerification;

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("{{USER_NAME}}", user.getFirstName());
		content = content.replace("{{EMAIL_ADDRESS}}", user.getEmail());
		content = content.replace("{{COMPANY_NAME}}", companyName);
		String verifyURL = siteURL + "/reset-password?code=" + verificationCode;

		content = content.replace("{{VERIFICATION_URL}}", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);

	}

	private void sendVerificationEmail(User user, String siteURL, String verificationCode)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = sender;
		String senderName = companyName;
		String subject = "Verification Email for Qr Based Attendence Management System";
		String content = emailTemplateForVerification;

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("{{USER_NAME}}", user.getFirstName());
		content = content.replace("{{EMAIL_ADDRESS}}", user.getEmail());
		content = content.replace("{{COMPANY_NAME}}", companyName);
		String verifyURL = siteURL + "/verify-email?code=" + verificationCode;

		content = content.replace("{{VERIFICATION_URL}}", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);

	}

	private final String emailTemplateForVerification = "<!DOCTYPE html><html lang=\"en\"><head><meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"><style type=\"text/css\">body{margin:0;background:#fefefe;color:#585858}table{font-size:15px;line-height:23px;max-width:500px;min-width:460px;text-align:center}.table_inner{min-width:100%!important}td{font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top}.carpool_logo{margin:30px auto}.dummy_row{padding-top:20px!important}.section,.sectionlike{background:#c9f9e9}.section{padding:0 20px}.sectionlike{padding-bottom:10px}.section_content{width:100%;background:#fff}.section_content_padded{padding:0 35px 40px}.section_zag{background:#f4fbf9}.imageless_section{padding-bottom:20px}img{display:block;margin:0 auto}.img_section{width:100%;max-width:500px}.img_section_side_table{width:100%!important}h1{font-size:20px;font-weight:500;margin-top:40px;margin-bottom:0}.near_title{margin-top:10px}.last{margin-bottom:0}a{color:#63d3cd;font-weight:500;word-break:break-word}.button{display:block;width:100%;max-width:300px;background:#20da9c;border-radius:8px;color:#fff;font-size:18px;font-weight:400;padding:12px 0;margin:30px auto 0;text-decoration:none}small{display:block;width:100%;max-width:330px;margin:14px auto 0;font-size:14px}.signature{padding:20px}.footer,.footer_like{background:#1fd99a}.footer{padding:0 20px 30px}.footer_content{width:100%;text-align:center;font-size:12px;line-height:initial;color:#005750}.footer_content a{color:#005750}.footer_item_image{margin:0 auto 10px}.footer_item_caption{margin:0 auto}.footer_legal{padding:20px 0 40px;margin:0;font-size:12px;color:#a5a5a5;line-height:1.5}.text_left{text-align:left}.text_right{text-align:right}.va{vertical-align:middle}.stats{min-width:auto!important;max-width:370px;margin:30px auto 0}.counter{font-size:22px}.stats_counter{width:23%}.stats_image{width:18%;padding:0 10px}.stats_meta{width:59%}.stats_spaced{padding-top:16px}.walkthrough_spaced{padding-top:24px}.walkthrough{max-width:none}.walkthrough_meta{padding-left:20px}.table_checkmark{padding-top:30px}.table_checkmark_item{font-size:15px}.td_checkmark{width:24px;padding:7px 12px 0 0}.padded_bottom{padding-bottom:40px}.marginless{margin:0}@media only screen and (max-width:480px) and (-webkit-min-device-pixel-ratio:2){table{min-width:auto!important}.section_content_padded{padding-right:25px!important;padding-left:25px!important}.counter{font-size:18px!important}}</style></head><body style=\"margin:0;background:#fefefe;color:#585858\"><span class=\"preheader\" style=\"display:none!important;visibility:hidden;opacity:0;color:transparent;height:0;width:0;border-collapse:collapse;border:0\"></span><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-size:15px;line-height:23px;max-width:500px;min-width:460px;text-align:center\"><tbody><tr><td style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important\"><img src=\"https://carpool-email-assets.s3.amazonaws.com/shared/carpool-logo@2x.png\" class=\"carpool_logo\" width=\"232\" style=\"display:block;margin:0 auto;margin:30px auto\"></td></tr><tr><td class=\"sectionlike imageless_section\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important;background:#c9f9e9;padding-bottom:10px;padding-bottom:20px\"></td></tr><tr><td class=\"section\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important;background:#c9f9e9;padding:0 20px\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"section_content\" style=\"font-size:15px;line-height:23px;max-width:500px;min-width:460px;text-align:center;width:100%;background:#fff\"><tbody><tr><td class=\"section_content_padded\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important;padding:0 35px 40px\"><h1 style=\"font-size:20px;font-weight:500;margin-top:40px;margin-bottom:0\">Hi {{USER_NAME}},</h1><p class=\"near_title last\" style=\"margin-top:10px;margin-bottom:0\">Please verify that your email address is {{EMAIL_ADDRESS}}, and that you entered it when signing up for {{COMPANY_NAME}}.</p><a href=\"{{VERIFICATION_URL}}\" style=\"display:block;width:100%;max-width:300px;background:#20da9c;border-radius:8px;color:#fff;font-size:18px;padding:12px 0;margin:30px auto 0;text-decoration:none\" target=\"_blank\">Verify email</a><small style=\"display:block;width:100%;max-width:330px;margin:14px auto 0;font-size:14px\">Wazers who carpool may see that you work at reallygoodemails.com, but your email address stays private.</small></td></tr></tbody></table></td></tr><tr><td class=\"section\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important;background:#c9f9e9;padding:0 20px\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"section_content section_zag\" style=\"font-size:15px;line-height:23px;max-width:500px;min-width:460px;text-align:center;width:100%;background:#fff;background:#f4fbf9\"><tbody><tr><td class=\"signature\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important;padding:20px\"><p class=\"marginless\" style=\"margin:0\">Happy carpooling,<br>The Waze Carpool Team</p></td></tr></tbody></table></td></tr><tr><td class=\"section dummy_row\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important;background:#c9f9e9;padding:0 20px;padding-top:20px!important\"></td></tr><tr><td style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"section_content\" style=\"font-size:15px;line-height:23px;max-width:500px;min-width:460px;text-align:center;width:100%;background:#fff\"><tbody><tr><td class=\"footer_like\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important;background:#1fd99a\"><img src=\"https://carpool-email-assets.s3.amazonaws.com/shared/footer@2x.png\" alt=\"\" width=\"500\" class=\"img_section\" style=\"display:block;margin:0 auto;width:100%;max-width:500px\"></td></tr><tr><td class=\"footer\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important;padding:0 20px 30px;background:#1fd99a\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"footer_content\" style=\"font-size:15px;line-height:23px;max-width:500px;min-width:460px;text-align:center;width:100%;font-size:12px;line-height:initial;color:#005750\"><tbody><tr><td width=\"33%\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important\"><img src=\"https://carpool-email-assets.s3.amazonaws.com/shared/footer-learn@2x.png\" width=\"24\" class=\"footer_item_image\" style=\"display:block;margin:0 auto;margin:0 auto 10px\"><p class=\"footer_item_caption\" style=\"margin:0 auto\">More about<br><a href=\"#\" style=\"color:#005750\" target=\"_blank\">{{COMPANY_NAME}}</a></p></td><td width=\"33%\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important\"><img src=\"https://carpool-email-assets.s3.amazonaws.com/shared/footer-support@2x.png\" width=\"24\" class=\"footer_item_image\" style=\"display:block;margin:0 auto;margin:0 auto 10px\"><p class=\"footer_item_caption\" style=\"margin:0 auto\">Questions?<br><a href=\"https://support.google.com/waze/carpool\" style=\"color:#005750\" target=\"_blank\">We're here</a></p></td><td width=\"33%\" style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important\"><img src=\"https://carpool-email-assets.s3.amazonaws.com/shared/footer-fb@2x.png\" width=\"24\" class=\"footer_item_image\" style=\"display:block;margin:0 auto;margin:0 auto 10px\"><p class=\"footer_item_caption\" style=\"margin:0 auto\">Join the community<br><a href=\"https://www.facebook.com/groups/wazecarpoolers\" style=\"color:#005750\" target=\"_blank\">on Facebook</a></p></td></tr></tbody></table></td></tr></tbody></table></td></tr><tr><td style=\"font-family:-apple-system,BlinkMacSystemFont,Roboto,sans-serif;vertical-align:top;border:none!important\"><p class=\"footer_legal\" style=\"padding:20px 0 40px;margin:0;font-size:12px;color:#a5a5a5;line-height:1.5\">If you did not enter this email address when signing up for Waze Carpool service, disregard this message.<br><br>© 2017 Google Inc. 1600 Amphitheatre Parkway, Mountain View, CA 94043<br><br>This is a mandatory service email from Waze Carpool.</p></td></tr></tbody></table></body></html>";
}
