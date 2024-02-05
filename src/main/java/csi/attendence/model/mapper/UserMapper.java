package csi.attendence.model.mapper;

import java.util.Optional;
import java.util.stream.Collectors;

import csi.attendence.entity.User;
import csi.attendence.entity.UserRole;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.UserResponse;

public class UserMapper {

    public static User mapToUser(UserRequest request, User user) {
    	
    	if(request==null) {
    		return null;
    	}
    	
        if (user == null) {
            user = new User();
        }
        
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
      
    	if(user==null) {
    		return null;
    	}
    	
    	if (response == null) {
            response = new UserResponse();
        }

        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setGender(user.getGender());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setRole(user.getRoles().stream().map(UserRole::getRole).collect(Collectors.toList()));
        response.setCreatedBy(user.getCreatedBy());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastModifiedBy(user.getLastModifiedBy());
        response.setLastModifiedAt(user.getLastModifiedAt());
        
        return response;
    }
}
