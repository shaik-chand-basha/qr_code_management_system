package csi.attendence.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import csi.attendence.entity.User;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.StudentResponse;

public interface CustomUserDetailsService extends UserDetailsService {


	User saveUser(UserRequest request, String siteUrl);

	StudentResponse findStudent(Long id);

	StudentResponse saveStudent(StudentRequest request, String siteUrl);

}
