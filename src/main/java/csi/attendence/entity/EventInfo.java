package csi.attendence.entity;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Builder
@Table(name = "event_info")
@Data
@EqualsAndHashCode(callSuper = true)
public class EventInfo extends BaseEntity {

	@Id
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name= "event_id")
	private Long eventId;

	private String title;

	private String description;

	private String whatsappGroupLink;

	private String venue;

	private Boolean active;

	@Temporal(TemporalType.DATE)
	private Date startingDate;

	@Temporal(TemporalType.DATE)
	private Date endingDate;

	@ManyToOne
	@JoinColumn(name = "fk_profile")
	private ImageMetadata fkProfile;
}
