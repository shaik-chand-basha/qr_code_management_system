package csi.attendence.controller.advice;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import csi.attendence.exceptions.AlreadyExistsException;
import csi.attendence.exceptions.BadRequestException;
import csi.attendence.exceptions.UserNotFoundException;
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

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(new Date()).error(ex.getMessage())
				.message(ex.getMessage()).path(request.getDescription(false).replace("uri=", "")).build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(new Date()).error(ex.getMessage())
				.message(ex.getMessage()).path(request.getDescription(false).replace("uri=", "")).build();

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(new Date()).error(ex.getMessage())
				.message(ex.getMessage()).path(request.getDescription(false).replace("uri=", "")).build();

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
		ex.printStackTrace();
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
		System.out.println(responseStatus);
		System.out.println("Response Status Handle Runtime Exception");
		ErrorResponse errorResponse = ErrorResponse.builder().timestamp(new Date()).error(ex.getMessage())
				.message(ex.getMessage()).path(request.getDescription(false).replace("uri=", "")).build();
		HttpStatus status = responseStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : responseStatus.code();
		return ResponseEntity.status(status).body(errorResponse);
	}

}
