package csi.attendence.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.constraints.groups.OnCreate;
import csi.attendence.model.request.EventRequest;
import csi.attendence.model.response.ApiResponse;
import csi.attendence.model.response.EventInfoResponse;
import csi.attendence.service.impl.EventServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(EventController.BASE_URL)
@Validated
@RequiredArgsConstructor
public class EventController {

	public static final String BASE_URL = "/api/v1/event";

	private final EventServiceImpl eventService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EventInfoResponse> createEvent(@Validated(OnCreate.class) @Valid EventRequest eventRequest,
			@Valid @NotNull MultipartFile eventPhoto) {
		EventInfoResponse eventInfoResponse = eventService.createEvent(eventRequest, eventPhoto);
		return ResponseEntity.ok(eventInfoResponse);
	}

	@PostMapping("/{event_id}/register/user/{user_id}")
	public ResponseEntity<ApiResponse> registerEvent(@Valid @NotNull @PathVariable("event_id") Long eventId,
			@Valid @NotNull @PathVariable("user_id") Long userId) {
		ApiResponse apiResponse = this.eventService.registerEvent(eventId, userId);
		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/user/{user_id}")
	public Object allEventsRegistered(@PathVariable("event_id") Long evetId, @PathVariable("user_id") Long userId) {
		
		return null;
	}

	@PatchMapping("/{event_id}")
	public Object updateEvent(@PathVariable("event_id") Long evetId,
			@RequestParam("eventPhoto") MultipartFile eventPhoto) {
		return null;
	}

	@PostMapping("/{event_id}/attendence/user/{user_id}")
	public ResponseEntity<ApiResponse> takeAttendenceToThisEvent(@PathVariable("event_id") Long evetId,
			@PathVariable("user_id") Long userId) {

		ApiResponse apiResponse = this.eventService.takeAttendence(evetId, userId);
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping(path="/{event_id}/upload_image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> uploadImagesToThisEvent(@PathVariable("event_id") Long evetId,
			@RequestParam("eventPhotos") @Size(max = 5,min = 1) List<MultipartFile> eventPhotos) {

		ApiResponse apiResponse = this.eventService.uploadImageToEvent(evetId, eventPhotos);
		return ResponseEntity.ok(apiResponse);
	}

	@DeleteMapping("/{event_id}")
	public Object deleteEvent(@PathVariable("event_id") Long evetId) {
		return null;
	}

	
	
	@GetMapping("/{event_id}")
	public Object findAllUsersRegisterdToThisEvent(@PathVariable("event_id") Long evetId) {
		return null;
	}

	@GetMapping("/{event_id}/user")
	public Object findAllRegisterdUsersAttendenceStatsToThisEvent(@PathVariable("event_id") Long evetId) {
		return null;
	}

}
