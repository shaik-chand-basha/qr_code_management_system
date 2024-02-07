package csi.attendence.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import csi.attendence.model.request.PasswordResetFinalRequest;
import csi.attendence.model.response.ApiResponse;
import csi.attendence.service.CustomUserDetailsService;
import csi.attendence.service.impl.EmailServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GeneralController {

	private final EmailServiceImpl emailService;

	private final CustomUserDetailsService customUserDetailsService;

	@GetMapping("/verify-email")
	public String validateEmail(@RequestParam("code") String validationCode) {
		this.emailService.validateEmailVerificationCode(validationCode);
		return "email_verification";
	}

	@GetMapping("/reset-password")
	public String passwordReset(@RequestParam("code") String validationCode, Model model) {

		try {
			emailService.validateResetPasswordVerificationCode(validationCode);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error",
					"Verification time is expired or it already used. Please try requesting it again.");
			return "password-reset-status";

		}
		model.addAttribute("verificationCode", validationCode);
		return "password-reset";
	}

	@PostMapping("/reset-password")
	public String passwordResetFinal(@Valid PasswordResetFinalRequest request, Model model) {

		try {

			ApiResponse apiResponse = this.customUserDetailsService.passwordChange(request);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error",
					"Verification time is expired or it already used. Please try requesting it again.");
			return "password-reset-status";

		}
		model.addAttribute("message",
				"password is successfully changed. all sessions will be logged out. please login again.");

		return "password-reset-status";
	}

}
