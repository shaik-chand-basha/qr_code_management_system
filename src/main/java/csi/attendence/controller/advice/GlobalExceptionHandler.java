package csi.attendence.controller.advice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import csi.attendence.model.response.ConstraintViolentResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ConstraintViolentResponse>> handleConstraintViolationException(
			MethodArgumentNotValidException ex) {
		
		
		List<ConstraintViolentResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ConstraintViolentResponse.builder().property(fieldError.getField()).message(fieldError.getDefaultMessage()).build())
                .collect(Collectors.toList());
		
		return ResponseEntity.badRequest().body(errors);
	}
}
