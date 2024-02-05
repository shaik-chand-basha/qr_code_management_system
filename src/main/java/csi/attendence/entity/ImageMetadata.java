package csi.attendence.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "image_metadata")
@EqualsAndHashCode(callSuper = true)
public class ImageMetadata extends BaseEntity {

	@Id
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	private String pathToImage;

	private String imageType;

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "created_by", nullable = true, updatable = false)
	private User createdBy;
}
