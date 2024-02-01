package csi.attendence.model.response;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

	private String accessToken;

	private String refreshToken;

	private Long expiresAt;

	private Date createdAt;

	private String type;

}
