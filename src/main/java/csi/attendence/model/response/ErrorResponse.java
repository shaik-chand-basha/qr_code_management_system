package csi.attendence.model.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	private String error;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z")
	private Date timestamp;

	private String message;

	private String path;
}
