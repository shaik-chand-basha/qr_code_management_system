package csi.attendence.model.request;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class StudentRequest {
	
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String mobileNumber;
	
	private Long hallticketNumber;
	
	private String csiID;
	
	private String className;
	
	private String college;
	
	private Integer year;
	
	private String address;
	
	@Lob
	private byte[] photo;
	
	private String password;
	

}
