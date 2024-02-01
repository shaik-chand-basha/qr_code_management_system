package csi.attendence.model.request;

import csi.attendence.enums.GenderEnum;
import lombok.Data;

@Data
public class UserRequest {

	private Long userId;

	private String firstName;

	private String lastName;

	private GenderEnum gender;

	private String password;

	private String email;

	private String mobileNumber;

	private String registrationType;

	private StudentRequest studentDetails;
	

}
