package csi.attendence.entity;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
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
@Table(name = "authentication_details")
@Data
@EqualsAndHashCode()
public class AuthenticationInfo {

	@Id
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	private String token;

	private String refreshToken;
	
	private Boolean active;

	@ManyToOne
	@JoinColumn(name = "fk_user_id", nullable = true, updatable = false)
	private User user;
	
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false, nullable = false, name = "created_at")
	private Date createdAt;


	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date lastModifiedAt;

}
