package csi.attendence.model.request;

import java.time.LocalDate;
import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordResetRequest {

	@NotBlank
	@Email
	private String email;
	
	@NotNull
	private LocalDate dob;
}
