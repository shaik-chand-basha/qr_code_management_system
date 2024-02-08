package csi.attendence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csi.attendence.entity.EventAttendence;
import csi.attendence.entity.EventInfo;
import csi.attendence.entity.User;

@Repository
public interface EventAttendenceRepository extends JpaRepository<EventAttendence, Long> {

	boolean existsByEventAndFkUser(EventInfo event, User fkUser);
}
