package csi.attendence.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import csi.attendence.entity.User;
import csi.attendence.model.mapper.UserMapper;
import csi.attendence.model.response.AuthenticationResponse;
import csi.attendence.model.response.UserResponse;
import csi.attendence.service.CustomUserDetailsService;
import csi.attendence.service.impl.JwtAuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(UserController.BASE_URL)
@RequiredArgsConstructor
public class UserController {

	public final static String BASE_URL = "/api/v1";

	private final CustomUserDetailsService customUserDetailsService;

	private final JwtAuthenticationServiceImpl authenticationService;

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(Authentication authentication) {
		if(authentication == null) {
			throw new UsernameNotFoundException("username not found");
		}
		User user = (User) authentication.getPrincipal();
		AuthenticationResponse authenticationInfo = authenticationService.generateAuthenticationInfo(user);
		return ResponseEntity.ok(authenticationInfo);
	}
	
	
	@PostMapping("/me")
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

}
