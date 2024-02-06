package csi.attendence.utils;

import java.io.File;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtils {

	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
	
	public static String pathToUrl(String path) {
		String name = new File(path).getName();
		return "/resource/%s".formatted(name);
	}
}
