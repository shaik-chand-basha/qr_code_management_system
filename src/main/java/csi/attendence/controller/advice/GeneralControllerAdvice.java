package csi.attendence.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import csi.attendence.exceptions.EmailVerificationExpiredException;

@ControllerAdvice
public class GeneralControllerAdvice {

	@ExceptionHandler(EmailVerificationExpiredException.class)
	public String handleEmailVerificationExpiredException(EmailVerificationExpiredException ex, WebRequest request) {
		return "email_verification-failed";
	}
}
