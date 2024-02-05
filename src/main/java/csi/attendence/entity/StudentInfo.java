package csi.attendence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "student_details")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentInfo extends BaseEntity {

	@Id
	@Column(name = "fk_user_id",insertable = false,updatable = false)
	private Long id;


	@MapsId
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_user_id", referencedColumnName = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "fk_approved_by")
	private User fkApprovedBy;

	private String address;

	@Column(updatable = false)
	private String hallticketNum;
	
	private String csiId;
	
	@Column(name = "class")
	private String className;
	
	private Integer yearOfJoin;
	
	private Boolean approved;

	private String college;



}
