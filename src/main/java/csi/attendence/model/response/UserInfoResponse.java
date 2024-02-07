package csi.attendence.model.response;

import csi.attendence.utils.UrlUtils;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoResponse {

	public UserInfoResponse(Long userId, String profileImage, String firstName, String lastName) {
		super();
		this.userId = userId;
		this.profileImage = UrlUtils.pathToUrl(profileImage);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	private Long userId;

	private String profileImage;

	private String firstName;

	private String lastName;
}
