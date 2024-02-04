package csi.attendence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import csi.attendence.entity.StudentInfo;

public interface StudentRepository extends JpaRepository<StudentInfo, Long> {

	boolean existsByCsiId(String csiId);
	boolean existsByHallticketNum(String hallticketNum);
}
