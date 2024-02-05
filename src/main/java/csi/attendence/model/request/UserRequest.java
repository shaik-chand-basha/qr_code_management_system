package csi.attendence.model.request;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.constraints.OnCreate;
import csi.attendence.enums.GenderEnum;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

	private Long userId;

	@NotBlank(groups = { OnCreate.class })
	private String firstName;

	private String lastName;

	@NotNull
	private GenderEnum gender;

	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dob;

	@Size(min = 8, max = 20)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).+$", message = "Password should contain atleast 1 capital letter, 1 Upper letter,1 Special letter,1 Number")
	private String password;

	@Email
	private String email;

	@Size(min = 10, max = 10)
	private String mobileNumber;


	@NotBlank(groups = { OnCreate.class })
	private String registrationType;

	@NotEmpty(groups = { OnCreate.class })
	private List<String> roles;

}