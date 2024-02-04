package csi.attendence.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.UserResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping(path = UserController.BASE_URL)
public class UserController {

	public final static String BASE_URL = "/api/v1";

	public ResponseEntity<UserResponse> registerStudent(@RequestBody UserRequest request) {
		return null;
	}
}
