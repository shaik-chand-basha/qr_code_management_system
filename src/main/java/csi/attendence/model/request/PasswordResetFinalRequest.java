package csi.attendence.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetFinalRequest {

	@NotBlank
	@Size(min = 8, max = 20)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).+$", message = "Password should contain atleast 1 capital letter, 1 Upper letter,1 Special letter,1 Number")
	private String password;

	@NotBlank
	private String verificationCode;
}
