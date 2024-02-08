package csi.attendence.listener.events;

import csi.attendence.entity.EventInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventCreatedEvent {

	private EventInfo event;
}
