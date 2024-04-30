package csi.attendence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csi.attendence.entity.EventInfo;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {

	List<EventInfo> findAllByActiveTrueAndStartingDateGreaterThanEqual(LocalDate startingDate);
	List<EventInfo> findAllByActiveTrueAndStartingDateGreaterThanEqualAndEndingDateLessThanEqual(LocalDate startingDate,LocalDate endingDate);
	List<EventInfo> findAllByActiveTrue();
	
	
}
