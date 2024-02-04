package csi.attendence.model.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Validated
public class StudentRequest {

	@NotBlank
	@Pattern(regexp = "^130[34]\\d{8}$")
	@Size(min = 12, max = 12)
	private String hallticketNum;

	@Size(min = 10)
	private String csiId;

	@NotBlank
	private String className;

	@NotBlank
	private String college;

	@PastOrPresent(message = "The year of joining must be in the present or the past")
	@NotBlank
	private LocalDate yearOfJoin;

	private String address;

	@NotNull
	private UserRequest userInfo;

	public int getYearOfJoining() {
		return this.yearOfJoin.getYear();
	}

}
