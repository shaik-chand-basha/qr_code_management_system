package csi.attendence.controller.advice;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import csi.attendence.exceptions.AlreadyExistsException;
import csi.attendence.model.response.ConstraintViolentResponse;
import csi.attendence.model.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ConstraintViolentResponse>> handleConstraintViolationException(
			MethodArgumentNotValidException ex) {

		List<ConstraintViolentResponse> errors = ex
				.getBindingResult().getFieldErrors().stream().map(fieldError -> ConstraintViolentResponse.builder()
						.property(fieldError.getField()).message(fieldError.getDefaultMessage()).build())
				.collect(Collectors.toList());

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(new Date()).error(ex.getMessage()).message(ex.getMessage())
				.path(request.getDescription(false).replace("uri=", "")).build();

		return ResponseEntity.badRequest().body(errorResponse);
	}

}
