package csi.attendence.model.request;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import csi.attendence.constraints.OnCreate;
import csi.attendence.constraints.OnUpdate;
import csi.attendence.enums.GenderEnum;
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

	@NotNull(groups = { OnCreate.class })
	private GenderEnum gender;

	@NotNull(groups = { OnCreate.class })
	@Past(groups = { OnCreate.class,OnUpdate.class })
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dob;
	
	@NotBlank(groups = { OnCreate.class })
	@Size(min = 8, max = 20,groups = { OnCreate.class,OnUpdate.class })
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).+$", message = "Password should contain atleast 1 capital letter, 1 Upper letter,1 Special letter,1 Number",groups = { OnCreate.class,OnUpdate.class })
	private String password;
	
	@NotBlank(groups = { OnCreate.class })
	@Email(groups = { OnCreate.class,OnUpdate.class })
	private String email;
	
	@NotBlank(groups = { OnCreate.class })
	@Size(min = 10, max = 10,groups = { OnCreate.class,OnUpdate.class })
	private String mobileNumber;


	@NotBlank(groups = { OnCreate.class })
	private String registrationType;

	@NotEmpty(groups = { OnCreate.class })
	private List<String> roles;

}
