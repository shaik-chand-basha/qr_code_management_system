package csi.attendence.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import csi.attendence.entity.User;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.StudentResponse;

public interface CustomUserDetailsService extends UserDetailsService {

	StudentResponse saveStudent(StudentRequest request);

	User saveUser(UserRequest request);

	StudentResponse findStudent(Long id);

}
