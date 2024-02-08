package csi.attendence.listener.events;

import csi.attendence.entity.EventInfo;
import csi.attendence.entity.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRegisteredToEventEvent {

	private EventInfo event;
	
	private User registeredUser;
}
