package csi.attendence.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import csi.attendence.entity.User;

public class AuthenticationUtils {

	public static User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			User loggedInUser = (User) authentication.getPrincipal();
			return loggedInUser;
		}
		return null;
	}

	public static boolean isUserAdmin(User user) {
		boolean isAdmin = false;

		if (user != null) {
			isAdmin = user.getRoles().stream().anyMatch(x -> x.getRole().equals("ROLE_ADMIN"));
		}
		return isAdmin;
	}
}
