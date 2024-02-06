package csi.attendence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

import csi.attendence.entity.AuthenticationInfo;
import csi.attendence.entity.User;

@Validated
public interface UserRepository extends JpaRepository<User, Long> {

//	@Query(value = "From User as u left join fetch u.roles where u.email=:username or u.userName=:username")
//	Optional<User> findUserByEmailOrMobileNumberOrUsername(@Valid @NotBlank String username);

	boolean existsByEmail(String email);

	boolean existsByMobileNumber(String mobile_number);


}
