package csi.attendence.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.entity.User;
import csi.attendence.enums.LoginType;
import csi.attendence.model.request.PasswordResetFinalRequest;
import csi.attendence.model.request.PasswordResetRequest;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.ApiResponse;
import csi.attendence.model.response.StudentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CustomUserDetailsService extends UserDetailsService {


	User saveUser(UserRequest request, String siteUrl);

	StudentResponse findStudent(Long id);

	StudentResponse saveStudent(StudentRequest request, String siteUrl);

	User findUserByCredentials(String authorizationHeader, LoginType loginType);

	ApiResponse updateProfileImage(MultipartFile file);

	ApiResponse resetPassoword(PasswordResetRequest request, HttpServletRequest request2);

	ApiResponse passwordChange(PasswordResetFinalRequest request);
	
	
	
	

}
