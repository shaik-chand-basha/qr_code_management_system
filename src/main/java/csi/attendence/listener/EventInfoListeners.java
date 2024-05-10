package csi.attendence.listener;

import org.springframework.stereotype.Component;

import csi.attendence.listener.events.EventCreatedEvent;
import csi.attendence.listener.events.UserRegisteredToEventEvent;
import csi.attendence.repository.EventInfoRepository;

@Component

public class EventInfoListeners {

	private final EventInfoRepository eventInfoRepository;

	public void listenEventCreated(EventCreatedEvent event) {
		System.out.println("Event created");
	}

	public void listenUserRegisteredToEventEvent(UserRegisteredToEventEvent event) {
		System.out.println("User registered to event");
		System.out.println(event.getRegisteredUser().getUsername());
	}

	public EventInfoListeners(EventInfoRepository eventInfoRepository) {
		super();
		this.eventInfoRepository = eventInfoRepository;
	}
}
