package csi.attendence.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException {

	public AlreadyExistsException(String className, String property, String value) {
		super("%s already exists with %s: %s".formatted(className, property, value));
	}

}
