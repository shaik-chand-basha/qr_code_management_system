package csi.attendence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import csi.attendence.entity.AuthenticationInfo;
import csi.attendence.entity.User;


@Repository
public interface AuthenticationRepository extends JpaRepository<AuthenticationInfo, Long> {

	@Query("FROM AuthenticationInfo auth  join fetch auth.user as user where auth.token=:token  and auth.active=true and user.active=true")
	Optional<AuthenticationInfo> findByToken(String token);

	@Query("FROM AuthenticationInfo auth  join fetch auth.user as user where auth.refreshToken=:token and auth.active=true and user.active=true")
	Optional<AuthenticationInfo> findByRefreshToken(String token);
	
	@Modifying
	void deleteByUser(User user);

}
