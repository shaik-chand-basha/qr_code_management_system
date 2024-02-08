package csi.attendence.model.request;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import csi.attendence.constraints.ValidYear;
import csi.attendence.constraints.groups.OnCreate;
import csi.attendence.constraints.groups.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
public class StudentRequest {

	@NotBlank(groups = { OnCreate.class })
	@Pattern(regexp = "^130[34]\\d{8}$",groups = { OnCreate.class,OnUpdate.class })
	@Size(min = 12, max = 12,groups = { OnCreate.class,OnUpdate.class })
	private String hallticketNum;

	@NotBlank(groups = { OnCreate.class })
	@Size(min = 10,groups = { OnCreate.class,OnUpdate.class })
	private String csiId;

	@NotBlank(groups = { OnCreate.class })
	private String className;

	@NotBlank(groups = { OnCreate.class })
	private String college;

	@ValidYear(past = true, present = true,groups = { OnCreate.class,OnUpdate.class })
	@NotNull(groups = { OnCreate.class })
	private Integer yearOfJoin;

	private String address;

	@NotNull(groups = { OnCreate.class })
	@Valid
	private UserRequest userInfo;
	
	
	private Boolean approved;

}
