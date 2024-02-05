package csi.attendence.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoResponse {

	private Long userId;

	private String profileImage;

	private String firstName;

	private String lastName;
}
