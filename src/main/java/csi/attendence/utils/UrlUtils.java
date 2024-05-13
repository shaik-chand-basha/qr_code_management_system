package csi.attendence.utils;

import java.io.File;
import java.util.Date;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UrlUtils {

	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
	
	public static String pathToUrl(String path) {
		if(Strings.isBlank(path)) {
			return null;
		}
		String name = new File(path).getName();
		return "/assets/img/db/%s".formatted(name);
	}
	
	public static void setSecureCookie(HttpServletResponse response, String name, String value, Date expiresAt) {
		ResponseCookie cookie = ResponseCookie.from(name, value).httpOnly(true) // Cookie not accessible via JavaScript
				.secure(true) // Cookie sent only over HTTPS
				.sameSite("Strict") // Cookie is not sent with cross-site requests
				.path("/") // Cookie available throughout the domain
				.maxAge((expiresAt.getTime() - System.currentTimeMillis()) / 1000) // Convert from milliseconds to													// seconds
				.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}
}
