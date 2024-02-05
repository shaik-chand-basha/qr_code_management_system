package csi.attendence.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.constraints.OnCreate;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.StudentResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(path = UserController.BASE_URL)

@Validated
public class UserController {

	public final static String BASE_URL = "/api/v1";

	@PostMapping(path = "/student/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StudentResponse> registerStudent(
			@RequestPart(required = true) @Validated(OnCreate.class) @Valid StudentRequest studentInfo,
			@RequestPart(required = true) @Validated(OnCreate.class) @Valid UserRequest userInfo,
			@RequestPart(required = true) @NotNull MultipartFile profileImage

	) {
		return null;
	}
}
