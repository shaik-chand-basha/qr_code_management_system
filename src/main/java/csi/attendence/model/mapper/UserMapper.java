package csi.attendence.model.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import csi.attendence.entity.User;
import csi.attendence.entity.UserRole;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.UserInfoResponse;
import csi.attendence.model.response.UserResponse;

public class UserMapper {

	public static User mapToUser(UserRequest request, User user) {

		if (request == null) {
			return null;
		}

		if (user == null) {
			user = new User();
		}
		Optional.ofNullable(request.getUserId()).ifPresent(user::setUserId);
		Optional.ofNullable(request.getFirstName()).ifPresent(user::setFirstName);
		Optional.ofNullable(request.getLastName()).ifPresent(user::setLastName);
		Optional.ofNullable(request.getGender()).ifPresent(user::setGender);
		Optional.ofNullable(request.getDob()).ifPresent(user::setDob);
		Optional.ofNullable(request.getPassword()).ifPresent(user::setPassword);
		Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);
		Optional.ofNullable(request.getMobileNumber()).ifPresent(user::setMobileNumber);

		return user;
	}

	public static UserResponse mapToUserResponse(User user, UserResponse response) {

		if (user == null) {
			return null;
		}

		if (response == null) {
			response = new UserResponse();
		}

		response.setUserInfo(toUserInfoResponse(user));
		response.setGender(user.getGender());
		response.setEmail(user.getEmail());
		response.setMobileNumber(user.getMobileNumber());
		response.setRoles(user.getRoles().stream().map(UserRole::getRole).collect(Collectors.toList()));

		response.setDob(user.getDob());
		response.setCreatedBy(toUserInfoResponse(user.getCreatedBy()));
		response.setCreatedAt(user.getCreatedAt());
		response.setLastModifiedBy(toUserInfoResponse(user.getLastModifiedBy()));
		response.setLastModifiedAt(user.getLastModifiedAt());
		if (!(user.getRoles() == null || user.getRoles().isEmpty())) {
			List<String> roles = user.getRoles().stream().map(x -> x.getRole()).collect(Collectors.toList());
			response.setRoles(roles);
		}
		return response;
	}

	public static UserInfoResponse toUserInfoResponse(User user) {
		if (user == null) {
			return null;
		}
		return UserInfoResponse.builder().userId(user.getUserId()).firstName(user.getFirstName())
				.lastName(user.getLastName())
				.profileImage(user.getFkProfile() != null ? user.getFkProfile().getPathToImage() : null).build();
	}
}
