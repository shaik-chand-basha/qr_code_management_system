package csi.attendence.model.mapper;

import csi.attendence.entity.StudentInfo;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.response.StudentResponse;

public class StudentMapper {

	public static StudentInfo mapToStudentInfo(StudentRequest request, StudentInfo studentInfo) {

		if (request == null) {
			return null;
		}

		if (studentInfo == null) {
			studentInfo = StudentInfo.builder().build();// Return null or throw an exception based on your error
														// handling policy
		}

		// Set fields from request to studentInfo if they are not null
		if (request.getHallticketNum() != null) {
			studentInfo.setHallticketNum(request.getHallticketNum());
		}
		if (request.getCsiId() != null) {
			studentInfo.setCsiId(request.getCsiId());
		}
		if (request.getClassName() != null) {
			studentInfo.setClassName(request.getClassName());
		}
		if (request.getYearOfJoin() != null) {
			studentInfo.setYearOfJoin(request.getYearOfJoin().toString());
		}
		if (request.getAddress() != null) {
			studentInfo.setAddress(request.getAddress());
		}

		return studentInfo;
	}

	public static StudentResponse mapToStudentResponse(StudentInfo studentInfo, StudentResponse response) {

		if (studentInfo == null) {
			return null;
		}

		if (response == null) {
			response = new StudentResponse();
		}

		response.setHallticketNumber(
				studentInfo.getHallticketNum() != null ? Long.parseLong(studentInfo.getHallticketNum()) : null);
		response.setCsiId(studentInfo.getCsiId());
		response.setClassName(studentInfo.getClassName());
		response.setCollege(studentInfo.getCollege());
		response.setYearOfJoin(studentInfo.getYearOfJoin());
		response.setAddress(studentInfo.getAddress());
		response.setApproved(studentInfo.getApproved() != null && studentInfo.getApproved());

		return response;
	}

}
