package csi.attendence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

import csi.attendence.entity.User;

@Validated
public interface UserRepository extends JpaRepository<User, Long> {

//	@Query(value = "From User as u left join fetch u.roles where u.email=:username or u.userName=:username")
//	Optional<User> findUserByEmailOrMobileNumberOrUsername(@Valid @NotBlank String username);

	@Query(value = "From User as u left join fetch u.roles where u.email=:email and active=true")
	Optional<User> findByEmailAndActive(String email);

	@Query("SELECT u FROM StudentInfo as st inner join  st.user as u where st.hallticketNum=:htNumber and u.active=true")
	Optional<User> findByHallticketNumber(Long htNumber);

	@Query(value = "From User as u left join fetch u.roles where u.mobileNumber=:mobileNumber and active=true")
	Optional<User> findByMobileNumberAndActiveTrue(String mobileNumber);

	boolean existsByEmail(String email);

	boolean existsByMobileNumber(String mobile_number);

}
