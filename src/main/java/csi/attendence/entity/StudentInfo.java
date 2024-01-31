package csi.attendence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Builder
@Table(name = "student_details")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentInfo extends BaseEntity {

	@Id
	@OneToOne
	@JoinColumn(name = "fk_user_id")
	private User id;

	@ManyToOne
	@JoinColumn(name = "fk_approved_by")
	private User fkApprovedBy;

	private String address;
	private String hallticketNum;
	private String csiId;
	private String yearOfJoin;
	private Boolean approved;

	@Column(name = "class")
	private String className;
}
