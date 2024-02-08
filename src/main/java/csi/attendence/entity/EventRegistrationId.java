package csi.attendence.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EventRegistrationId {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_user_id", nullable = false, updatable = false)
	private User fkUser;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_event_id", nullable = false,updatable = false)
	private EventInfo event;

}
