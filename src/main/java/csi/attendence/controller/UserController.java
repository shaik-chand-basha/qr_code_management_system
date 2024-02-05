package csi.attendence.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import csi.attendence.constraints.OnCreate;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.response.StudentResponse;
import csi.attendence.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = UserController.BASE_URL)
@RequiredArgsConstructor
@Validated
public class UserController {

	public final static String BASE_URL = "/api/v1";

	private final CustomUserDetailsService customUserDetailsService;

	@PostMapping(path = "/student/register")
	public ResponseEntity<StudentResponse> registerStudent(@Validated(OnCreate.class) @Valid @RequestBody  StudentRequest studentInfo) {
		System.out.println(studentInfo.getYearOfJoin());
		StudentResponse saveStudent = customUserDetailsService.saveStudent(studentInfo);

		return ResponseEntity.ok(saveStudent);
	}
}
