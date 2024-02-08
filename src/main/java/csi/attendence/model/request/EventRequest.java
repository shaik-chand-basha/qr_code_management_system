package csi.attendence.model.request;

import java.time.LocalDate;

import csi.attendence.constraints.groups.OnCreate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventRequest {

	@NotBlank(groups = { OnCreate.class })
	@Size(min = 10,max = 250)
	private String title;

	@NotBlank(groups = { OnCreate.class })
	private String description;

	@NotBlank(groups = { OnCreate.class })
	@Size(min = 10,max = 250)
	private String venue;

	@NotNull(groups = { OnCreate.class })
	@FutureOrPresent
	private LocalDate startingDate;

	@NotNull(groups = { OnCreate.class })
	@Future
	private LocalDate endingDate;

}
