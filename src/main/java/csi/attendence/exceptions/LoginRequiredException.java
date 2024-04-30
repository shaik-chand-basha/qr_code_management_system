package csi.attendence.exceptions;

public class LoginRequiredException extends RuntimeException {
	public LoginRequiredException() {
		super();
	}

	public LoginRequiredException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public LoginRequiredException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public LoginRequiredException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public LoginRequiredException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
