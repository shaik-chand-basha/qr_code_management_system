package csi.attendence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csi.attendence.entity.EventRegistration;
import csi.attendence.entity.EventRegistrationId;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, EventRegistrationId> {

	
}
