package csi.attendence.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.FORBIDDEN,reason = "Access token is expired. please aquire new one")
public class AccessTokenExpiredException extends LoginRequiredException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccessTokenExpiredException() {
		super("Access token is expired. please aquire new one");
	}

}
