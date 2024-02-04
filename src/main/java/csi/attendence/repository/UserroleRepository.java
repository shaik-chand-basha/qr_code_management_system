package csi.attendence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import csi.attendence.entity.UserRole;

public interface UserroleRepository extends JpaRepository<UserRole, Integer> {

	List<UserRole> findByRoleIn(List<String> roles);
}
