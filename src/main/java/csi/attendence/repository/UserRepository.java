package csi.attendence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

import csi.attendence.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Validated
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "From User as u left join fetch u.roles where u.email=:username or u.mobile_number=:username or u.username=:username limit 1")
	Optional<User> findUserByEmailOrMobileNumberOrUsername(@Valid @NotBlank String username);

	boolean existsByEmail(String email);

	boolean existsByMobile_number(String mobile_number);

}
