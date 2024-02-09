package csi.attendence.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "event_photos")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventPhoto extends BaseEntity {

	@Id
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fk_event_id")
	private EventInfo event;

	@ManyToOne
	@JoinColumn(name = "fk_photo")
	private ImageMetadata photo;

}
