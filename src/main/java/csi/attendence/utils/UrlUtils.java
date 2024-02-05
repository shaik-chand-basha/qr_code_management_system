package csi.attendence.utils;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtils {

	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
}
