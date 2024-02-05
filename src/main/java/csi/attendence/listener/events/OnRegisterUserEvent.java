package csi.attendence.listener.events;

import csi.attendence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnRegisterUserEvent {

	private User user;

	private String siteURL;

}
