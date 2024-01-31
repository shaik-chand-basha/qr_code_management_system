package csi.attendence.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@MappedSuperclass
public class BaseEntity {

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false, updatable = false)
	private User createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false, nullable = false, name = "created_at")
	private Date createdAt;

	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name = "last_modified_by", nullable = true)
	private User lastModifiedBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date lastModifiedAt;

}
