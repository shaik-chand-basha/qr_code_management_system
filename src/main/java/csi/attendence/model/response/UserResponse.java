package csi.attendence.model.response;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import csi.attendence.enums.GenderEnum;
import lombok.Data;

@Data
public class UserResponse {

	@JsonUnwrapped
	private UserInfoResponse userInfo;

	private GenderEnum gender;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dob;

	private String email;

	private String mobileNumber;

	private List<String> roles;

	private UserInfoResponse createdBy;

	private Date createdAt;

	private UserInfoResponse lastModifiedBy;

	private Date lastModifiedAt;

}
