package csi.attendence.model.request;

import lombok.Data;

@Data
public class StudentRequest {

	private Long hallticketNumber;

	private String csiId;

	private String className;

	private String college;

	private String yearOfJoin;

	private String address;


}
