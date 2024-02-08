package csi.attendence.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.entity.EventAttendence;
import csi.attendence.entity.EventInfo;
import csi.attendence.entity.EventRegistration;
import csi.attendence.entity.EventRegistrationId;
import csi.attendence.entity.ImageMetadata;
import csi.attendence.entity.User;
import csi.attendence.exceptions.AlreadyExistsException;
import csi.attendence.exceptions.BadRequestException;
import csi.attendence.model.mapper.EventMapper;
import csi.attendence.model.request.EventRequest;
import csi.attendence.model.response.ApiResponse;
import csi.attendence.model.response.EventInfoResponse;
import csi.attendence.repository.EventAttendenceRepository;
import csi.attendence.repository.EventInfoRepository;
import csi.attendence.repository.EventRegistrationRepository;
import csi.attendence.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventServiceImpl {

	private final EventInfoRepository eventInfoRepository;

	private final EventRegistrationRepository eventRegistrationRepository;

	private final EventAttendenceRepository eventAttendenceRepository;

	private final UserRepository userRepository;

	private final ImageMetadataServiceImpl imageMetadataService;

	public EventInfoResponse createEvent(EventRequest request, MultipartFile image) {
		if (request == null) {
			throw new BadRequestException("EventInfo required");
		}
		if (image.isEmpty()) {
			throw new BadRequestException("Event profile photo required");
		}
		boolean after = request.getStartingDate().isAfter(request.getEndingDate());
		if (after) {
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
		eventInfo.setActive(true);
		EventInfo savedEventInfo = this.eventInfoRepository.save(eventInfo);

		return EventMapper.mapToEventInfoResponse(savedEventInfo);

	}

	public ApiResponse registerEvent(Long eventId, Long userId) {

		EventInfo eventInfo = this.eventInfoRepository.findById(eventId)
				.orElseThrow(() -> new BadRequestException("Event not found with id %d".formatted(eventId)));

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new BadRequestException("User not found with id %d".formatted(userId)));

		EventRegistrationId eventRegistrationId = new EventRegistrationId(user, eventInfo);
		this.eventRegistrationRepository.findById(eventRegistrationId).ifPresent(eventRegistration -> {
			throw new AlreadyExistsException("User already registered for this event");
		});
		EventRegistration eventRegistration = new EventRegistration();
		eventRegistration.setId(eventRegistrationId);
		this.eventRegistrationRepository.save(eventRegistration);
		ApiResponse apiResponse = ApiResponse.builder().status(HttpStatus.OK).message("User registered successfulyy")
				.build();
		return apiResponse;
	}

	public ApiResponse takeAttendence(Long eventId, Long userId) {

		EventInfo eventInfo = this.eventInfoRepository.findById(eventId)
				.orElseThrow(() -> new BadRequestException("Event not found with id %d".formatted(eventId)));

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new BadRequestException("User not found with id %d".formatted(userId)));

		EventRegistrationId eventRegistrationId = new EventRegistrationId(user, eventInfo);
		this.eventRegistrationRepository.findById(eventRegistrationId)
				.orElseThrow(() -> new BadRequestException("User is not registered for this event"));
		boolean alreadyAttendenceTaken = this.eventAttendenceRepository.existsByEventAndFkUser(eventInfo, user);
		if(alreadyAttendenceTaken) {
			throw new AlreadyExistsException("User already have given attendence");
		}
		EventAttendence eventAttendence = new EventAttendence();
		eventAttendence.setApproved(true);
		eventAttendence.setAttendenceDatetime(new Date());
		eventAttendence.setEvent(eventInfo);
		eventAttendence.setFkUser(user);
		eventAttendence.setLocation(null);
		eventAttendence.setScreenshot(null);
		this.eventAttendenceRepository.save(eventAttendence);

		ApiResponse apiResponse = ApiResponse.builder().status(HttpStatus.OK).message("User attendence saved successfulyy")
				.build();
		return apiResponse;
	}

	public List<EventInfoResponse> findAllEventsRegisteredByUser(Long userId) {
//		this.eventRegistrationRepository.findBy
		return new ArrayList<>();
	}
}
