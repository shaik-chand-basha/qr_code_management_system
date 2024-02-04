package csi.attendence.model.mapper;

import csi.attendence.entity.User;
import csi.attendence.model.request.UserRequest;

public class UserMapper {

	public static User mapToUser(UserRequest request, User user) {
		if (user == null) {
			user = new User();
		}

		return user;
	}

	public static UserRequest mapToUserRequest(User user, UserRequest request) {
		if (request == null) {
			request = new UserRequest();
		}

		return request;
	}
}
