package csi.attendence.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRoleRequest {

	@NotBlank
	private Long userId;

	@NotBlank
	private String role;
}
