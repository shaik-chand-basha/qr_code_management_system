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

@Entity
@Table(name = "email_verification")
@Data
public class EmailVerification {
	

	@Id
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fk_user_id", nullable = false, updatable = false)
	private User fkUserId;

	private String otp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date otpGeneratedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date otpExpires;

	private Boolean active;

	private Boolean emailVerified;

}
