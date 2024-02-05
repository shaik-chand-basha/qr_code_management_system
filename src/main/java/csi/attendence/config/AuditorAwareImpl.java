package csi.attendence.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import csi.attendence.entity.User;
import csi.attendence.utils.AuthenticationUtils;

@Component("AuditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional.ofNullable(AuthenticationUtils.getLoggedInUser());
	}

}
