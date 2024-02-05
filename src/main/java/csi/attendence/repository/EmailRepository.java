package csi.attendence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import csi.attendence.entity.EmailVerification;

@Repository
public interface EmailRepository extends JpaRepository<EmailVerification, Long> {

	@Query("from EmailVerification as token where token.token=:token and token.active=true and token.emailVerified=false")
	Optional<EmailVerification> findByToken(String token);
}
