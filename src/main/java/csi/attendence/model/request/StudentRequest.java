package csi.attendence.model.request;

import org.springframework.validation.annotation.Validated;

import csi.attendence.constraints.OnCreate;
import csi.attendence.constraints.ValidYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Validated
public class StudentRequest {

	@NotBlank(groups = { OnCreate.class })
	@Pattern(regexp = "^130[34]\\d{8}$")
	@Size(min = 12, max = 12)
	private String hallticketNum;

	@NotBlank(groups = { OnCreate.class })
	@Size(min = 10)
	private String csiId;

	@NotBlank(groups = { OnCreate.class })
	private String className;

	@NotBlank(groups = { OnCreate.class })
	private String college;

	@ValidYear(past = true, present = true)
	@NotBlank(groups = { OnCreate.class })
	private Integer yearOfJoin;

	private String address;

	
	@NotBlank(groups = { OnCreate.class })
	private UserRequest userInfo;

}
