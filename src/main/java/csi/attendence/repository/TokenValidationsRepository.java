package csi.attendence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import csi.attendence.entity.TokenValidations;
import csi.attendence.entity.User;
import java.util.List;

@Repository
public interface TokenValidationsRepository extends JpaRepository<TokenValidations, Long> {

	@Modifying
	void deleteByUser(User user);

	Optional<TokenValidations> findByTokenAndActive(String token, boolean active);

	@Modifying
	@Query("DELETE TokenValidations as token where token=:token")
	void deleteByToken(String token);
}
