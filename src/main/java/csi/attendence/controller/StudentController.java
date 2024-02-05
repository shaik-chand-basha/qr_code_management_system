package csi.attendence.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(path = StudentController.BASE_URL)
@RequiredArgsConstructor
@Validated
public class StudentController {

	public final static String BASE_URL = "/api/v1";

	private final CustomUserDetailsService customUserDetailsService;

	@PostMapping(path = "/student/register")
	public ResponseEntity<StudentResponse> registerStudent(
			@Validated(OnCreate.class) @Valid @RequestBody StudentRequest studentInfo) {
		System.out.println(studentInfo.getYearOfJoin());
		StudentResponse saveStudent = customUserDetailsService.saveStudent(studentInfo);

		return ResponseEntity.ok(saveStudent);
	}

	@GetMapping("/student/{user_id}")
	public ResponseEntity<StudentResponse> findStudentById(@PathVariable("user_id") Long id) {
		StudentResponse studentResponse = this.customUserDetailsService.findStudent(id);
		return ResponseEntity.ok(studentResponse);
	}
}
