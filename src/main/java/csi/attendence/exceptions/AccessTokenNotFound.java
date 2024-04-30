package csi.attendence.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.UNAUTHORIZED,reason = "Access token not found, Please log in")
public class AccessTokenNotFound extends LoginRequiredException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccessTokenNotFound() {
		super("Access token not found, Please log in");
	}

}
