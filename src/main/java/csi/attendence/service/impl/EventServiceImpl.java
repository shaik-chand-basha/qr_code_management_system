package csi.attendence.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.entity.EventInfo;
import csi.attendence.entity.ImageMetadata;
import csi.attendence.entity.User;
import csi.attendence.exceptions.BadRequestException;
import csi.attendence.model.mapper.EventMapper;
import csi.attendence.model.request.EventRequest;
import csi.attendence.model.response.EventInfoResponse;
import csi.attendence.repository.EventInfoRepository;
import csi.attendence.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventServiceImpl {

	private final EventInfoRepository eventInfoRepository;

	private final ImageMetadataServiceImpl imageMetadataService;

	public EventInfoResponse createEvent(EventRequest request, MultipartFile image) {
		if (request == null) {
			throw new BadRequestException("EventInfo required");
		}
		if (image.isEmpty()) {
			throw new BadRequestException("Event profile photo required");
		}
		boolean after = request.getStartingDate().isAfter(request.getEndingDate());
		if(after) {
			throw new BadRequestException("Invalid starting date recieved.");
		}

		ImageMetadata imageMetadata = null;
		try {
			imageMetadata = imageMetadataService.saveImageMetadata(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventInfo eventInfo = EventMapper.mapToEventInfo(request, new EventInfo());
		eventInfo.setFkProfile(imageMetadata);
		EventInfo savedEventInfo = this.eventInfoRepository.save(eventInfo);

		return EventMapper.mapToEventInfoResponse(savedEventInfo);

	}
}
