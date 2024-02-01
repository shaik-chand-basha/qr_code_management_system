package csi.attendence.service.impl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import csi.attendence.entity.User;
import csi.attendence.exceptions.BadRequestException;
import csi.attendence.model.request.UserRequest;
import csi.attendence.repository.UserRepository;
import csi.attendence.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private final UserRepository userRepository;

	public User saveUser(UserRequest request) {
		if (request == null) {
			throw new BadRequestException("userinfo should not be empty.");
		}
		
		return null;
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		if (Strings.isBlank(username)) {
			throw new UsernameNotFoundException("username should not be empty.");
		}
		String usernameNotFoundMessage = String.format("user with username: %s not found.", username);
		User user = this.userRepository.findUserByEmailOrMobileNumberOrUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(usernameNotFoundMessage));
		return user;
	}

}
