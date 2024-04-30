package csi.attendence;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import csi.attendence.entity.User;
import csi.attendence.entity.UserRole;
import csi.attendence.enums.GenderEnum;
import csi.attendence.repository.UserRepository;
import csi.attendence.repository.UserroleRepository;

@EnableAsync
@SpringBootApplication(scanBasePackages = "csi.attendence")
@EnableJpaAuditing(auditorAwareRef = "AuditorAwareImpl", dateTimeProviderRef = "auditingDateTimeProvider")
public class QrCodeStudentAttendenceManagementSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(QrCodeStudentAttendenceManagementSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder,
			UserroleRepository roleRepository) {
		return args -> {
			if (userRepository.findByEmailAndActive("admin@csi.com").isEmpty()) {
				User adminUser = new User();
				adminUser.setFirstName("Admin");
				adminUser.setLastName("User");
				adminUser.setEmail("admin@csi.com");
				adminUser.setMobileNumber("8206646306");
				adminUser.setGender(GenderEnum.M);
				adminUser.setDob(new Date()); // Set a default or calculated date
				adminUser.setPassword(passwordEncoder.encode("admin"));
				adminUser.setActive(true);

				List<UserRole> adminRole = roleRepository.findByRoleIn(List.of("ROLE_ADMIN"));
				adminUser.setRoles(adminRole);

				userRepository.save(adminUser);
			}
		};
	}

}
