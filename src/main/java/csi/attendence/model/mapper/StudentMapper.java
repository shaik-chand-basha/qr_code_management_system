package csi.attendence.model.mapper;

import csi.attendence.entity.StudentInfo;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.response.StudentResponse;
import csi.attendence.model.response.UserResponse;

public class StudentMapper {

	public static StudentInfo mapToStudentInfo(StudentRequest request, StudentInfo studentInfo) {

		if (request == null) {
			return null;
		}

		if (studentInfo == null) {
			studentInfo = new StudentInfo();
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
			studentInfo.setYearOfJoin(request.getYearOfJoin());
		}
		if(request.getCollege() != null) {
			studentInfo.setCollege(request.getCollege());
		}
		if (request.getAddress() != null) {
			studentInfo.setAddress(request.getAddress());
		}
		if(request.getApproved() != null) {
			studentInfo.setApproved(request.getApproved());
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
		response.setYearOfJoin(studentInfo.getYearOfJoin().toString());
		response.setAddress(studentInfo.getAddress());
		response.setApproved(studentInfo.getApproved() != null && studentInfo.getApproved());
		response.setUserInfo(UserMapper.mapToUserResponse(studentInfo.getUser(), new UserResponse()));
		if (studentInfo.getFkApprovedBy() != null) {
			response.setApprovedBy(UserMapper.toUserInfoResponse(studentInfo.getFkApprovedBy()));
		}
		return response;
	}

}
