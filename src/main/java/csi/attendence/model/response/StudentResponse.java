package csi.attendence.model.response;

import lombok.Data;

@Data
public class StudentResponse {

	private Long hallticketNumber;

	private String csiId;

	private String className;

	private String college;

	private String yearOfJoin;

	private String address;
	
	private boolean approved;
	
	
	

}
