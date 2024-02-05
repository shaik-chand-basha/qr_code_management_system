package csi.attendence.entity;

import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import csi.attendence.enums.GenderEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "user_info")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long userId;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false, columnDefinition = "ENUM('M', 'F', 'O')")
//	@JdbcTypeCode(Types.E)
	private GenderEnum gender;


	private String password;

	@ManyToOne
	@JoinColumn(name = "fk_profile")
	private ImageMetadata fkProfile;

	@ManyToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private List<UserRole> roles;

	private String email;

	private String mobileNumber;
	
	@Temporal(TemporalType.DATE)
	private Date dob;

	@JdbcTypeCode(value = Types.BIT)
	private Boolean active;

	@CreatedBy
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "created_by", nullable = true, updatable = false)
	private User createdBy;

	@LastModifiedBy
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "last_modified_by", nullable = true)
	private User lastModifiedBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false, nullable = false)
	private Date createdAt;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date lastModifiedAt;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.roles;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return List.of(this.firstName, this.lastName).stream().filter(x -> Strings.isNotBlank(x))
				.collect(Collectors.joining(" "));
	}

	@Override
	public boolean isAccountNonExpired() {

		return false;
	}

	@Override
	public boolean isAccountNonLocked() {

		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.active;
	}

}
