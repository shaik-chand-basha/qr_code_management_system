package csi.attendence.model.mapper;

import java.util.Optional;
import csi.attendence.entity.EventInfo;
import csi.attendence.model.request.EventRequest;
import csi.attendence.model.response.EventInfoResponse;
import csi.attendence.utils.UrlUtils;

public class EventMapper {

	public static EventInfo mapToEventInfo(EventRequest request, EventInfo eventInfo) {
		if (request == null) {
			return null;
		}

		if (eventInfo == null) {
			eventInfo = new EventInfo();
		}

		Optional.ofNullable(request.getTitle()).ifPresent(eventInfo::setTitle);
		Optional.ofNullable(request.getDescription()).ifPresent(eventInfo::setDescription);
		Optional.ofNullable(request.getVenue()).ifPresent(eventInfo::setVenue);
		Optional.ofNullable(request.getStartingDate()).ifPresent(eventInfo::setStartingDate);
		Optional.ofNullable(request.getEndingDate()).ifPresent(eventInfo::setEndingDate);

		return eventInfo;
	}

	public static EventInfoResponse mapToEventInfoResponse(EventInfo eventInfo) {
		if (eventInfo == null) {
			return null;
		}

		EventInfoResponse response = EventInfoResponse.builder().eventId(eventInfo.getEventId())
				.title(eventInfo.getTitle()).description(eventInfo.getDescription())
				.whatsappGroupLink(eventInfo.getWhatsappGroupLink()).venue(eventInfo.getVenue())
				.active(eventInfo.getActive()).startingDate(eventInfo.getStartingDate())
				.endingDate(eventInfo.getEndingDate())
				.profileImage(
						eventInfo.getFkProfile() != null ? UrlUtils.pathToUrl(eventInfo.getFkProfile().getPathToImage())
								: null)
				.createdAt(eventInfo.getCreatedAt()).lastModifiedAt(eventInfo.getLastModifiedAt())
				.createdBy(UserMapper.toUserInfoResponse(eventInfo.getCreatedBy()))
				.lastModifiedBy(UserMapper.toUserInfoResponse(eventInfo.getLastModifiedBy())).build();

		return response;
	}
}
