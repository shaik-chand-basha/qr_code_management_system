package csi.attendence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csi.attendence.entity.EventInfo;

@Repository
public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {


}
