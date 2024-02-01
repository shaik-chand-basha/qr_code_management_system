package csi.attendence.model.response;

import java.util.Date;

import csi.attendence.entity.User;
import csi.attendence.enums.GenderEnum;
import lombok.Data;

@Data
public class UserResponse {

	private Long userId;

	private String firstName;

	private String lastName;

	private GenderEnum gender;

	private String email;

	private String mobileNumber;

	private String role;

	private User createdBy;

	private Date createdAt;

	private User lastModifiedBy;

	private Date lastModifiedAt;

	private StudentResponse studentDetails;
}
