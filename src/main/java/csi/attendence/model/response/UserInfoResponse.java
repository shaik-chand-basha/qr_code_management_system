package csi.attendence.model.response;

import java.util.Date;

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
	
	public UserInfoResponse(Long userId, String profileImage, String firstName, String lastName,Date attendenceTime) {
		super();
		this.userId = userId;
		this.profileImage = UrlUtils.pathToUrl(profileImage);
		this.firstName = firstName;
		this.lastName = lastName;
		this.attendenceTime = attendenceTime;
	}

	private Long userId;

	private String profileImage;

	private String firstName;

	private String lastName;
	
	private Date attendenceTime;
}
