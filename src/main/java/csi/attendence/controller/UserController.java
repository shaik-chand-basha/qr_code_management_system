package csi.attendence.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.constraints.groups.OnUserSearch;
import csi.attendence.entity.User;
import csi.attendence.model.mapper.UserMapper;
import csi.attendence.model.request.PasswordResetRequest;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.ApiResponse;
import csi.attendence.model.response.AuthenticationResponse;
import csi.attendence.model.response.UserInfoResponse;
import csi.attendence.model.response.UserResponse;
import csi.attendence.service.CustomUserDetailsService;
import csi.attendence.service.impl.EmailServiceImpl;
import csi.attendence.service.impl.JwtAuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(UserController.BASE_URL)
@RequiredArgsConstructor
public class UserController {

	public final static String BASE_URL = "/api/v1";
	private final EmailServiceImpl emailService;

	private final CustomUserDetailsService customUserDetailsService;

	private final JwtAuthenticationServiceImpl authenticationService;

	@GetMapping("/verify-email")
	public ResponseEntity<String> validateEmail(@RequestParam("code") String validationCode) {
		this.emailService.validateEmailVerificationCode(validationCode);
		return ResponseEntity.ok().body("Verification Successful");
	}

	@PutMapping(path = "/user/me/profile/image")
	public ResponseEntity<ApiResponse> uploadImage(@RequestParam("profileImage") MultipartFile file) {
		ApiResponse apiResponse = this.customUserDetailsService.updateProfileImage(file);
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(Authentication authentication,HttpServletResponse response) {
		if (authentication == null) {
			throw new UsernameNotFoundException("username not found");
		}
		User user = (User) authentication.getPrincipal();
		AuthenticationResponse authResponse = authenticationService.generateAuthenticationInfo(user);
        setSecureCookie(response, "accessToken", authResponse.getAccessToken(), authResponse.getExpiresAt());
        setSecureCookie(response, "refreshToken", authResponse.getRefreshToken(), authResponse.getRefreshTokenExpiresAt());

		return ResponseEntity.status(HttpStatus.OK).body(authResponse);
	}

	@PostMapping("/user/me")
	public ResponseEntity<UserResponse> me(Authentication authentication) {
		System.out.println("me");
		User user = (User) authentication.getPrincipal();
		UserResponse userResponse = UserMapper.mapToUserResponse(user, null);
		return ResponseEntity.ok(userResponse);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
		String refreshTokenHeader = request.getHeader("X-API-REFRESH_TOKEN");
		AuthenticationResponse authenticationInfo = authenticationService
				.generateAuthenticationInfoByRefreshToken(refreshTokenHeader);
		return ResponseEntity.ok(authenticationInfo);
	}

	@PostMapping("/user/reset-password")
	public ResponseEntity<ApiResponse> passwordReset(@Valid @RequestBody PasswordResetRequest passwordResetRequest,
			HttpServletRequest request) {

		ApiResponse apiResponse = this.customUserDetailsService.resetPassoword(passwordResetRequest, request);
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("/search/user")
	public ResponseEntity<List<UserInfoResponse>> findEmailByDobAndName(
			@Validated(OnUserSearch.class) @Valid @RequestBody UserRequest request) {
		List<UserInfoResponse> list = this.customUserDetailsService.findUsersByFirstNameAndLastName(request);
		return ResponseEntity.ok(list);
	}
	
	 private void setSecureCookie(HttpServletResponse response, String name, String value, Date expiresAt) {
	        ResponseCookie cookie = ResponseCookie.from(name, value)
	                .httpOnly(true)   // Cookie not accessible via JavaScript
	                .secure(true)    // Cookie sent only over HTTPS
	                .sameSite("Strict") // Cookie is not sent with cross-site requests
	                .path("/")       // Cookie available throughout the domain
	                .maxAge((expiresAt.getTime() - System.currentTimeMillis()) / 1000) // Convert from milliseconds to seconds
	                .build();
	        
	        response.addHeader("Set-Cookie", cookie.toString());
	    }

}
