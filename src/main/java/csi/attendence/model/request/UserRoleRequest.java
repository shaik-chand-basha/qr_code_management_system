package csi.attendence.model.request;

import lombok.Data;

@Data
public class UserRoleRequest {

	private Long userId;

	private String role;
}
