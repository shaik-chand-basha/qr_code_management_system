package csi.attendence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Builder
@Table(name = "event_registration")
@Data
@EqualsAndHashCode(callSuper = true)

public class EventRegistration extends BaseEntity {

	@EmbeddedId
	private EventRegistrationId id;

}
