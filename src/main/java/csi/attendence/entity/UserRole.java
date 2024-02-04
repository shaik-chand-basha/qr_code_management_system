package csi.attendence.entity;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "user_role")
public class UserRole implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="role_id")
	private Integer id;

	@Column(name="user_role")
	private String role;

	@ManyToMany
    @JoinTable(
        name="user_info_role",
        joinColumns=
            @JoinColumn(name="fk_role_id", referencedColumnName="role_id"),
        inverseJoinColumns=
            @JoinColumn(name="fk_user_id", referencedColumnName="user_id")
    )
	private List<User> user;

	@Override
	public String getAuthority() {
		return this.role;
	}

}
