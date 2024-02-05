package csi.attendence.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConstraintViolentResponse {

	private String property;
	
	private String message;
}
