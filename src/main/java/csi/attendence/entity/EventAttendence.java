package csi.attendence.entity;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "event_attendence")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventAttendence extends BaseEntity {

	@Id
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fk_user_id", nullable = false, updatable = false)
	private User fkUser;

	@ManyToOne
	@JoinColumn(name = "fk_event_id", nullable = false, updatable = false)
	private EventInfo event;

	@Temporal(TemporalType.TIMESTAMP)
	private Date attendenceDatetime;

	private String location;

	private Boolean approved;

	@ManyToOne
	@JoinColumn(name = "fk_screenshot")
	private ImageMetadata screenshot;

}
