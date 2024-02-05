package csi.attendence.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import csi.attendence.service.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GeneralController {

	private final  EmailServiceImpl emailService;
	
	@GetMapping("/verify-email")
	public String validateEmail( @RequestParam("code") String validationCode) {
		this.emailService.validateEmailVerificationCode(validationCode);
		return "email_verification";
	}
}
