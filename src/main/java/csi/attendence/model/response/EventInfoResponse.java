package csi.attendence.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EventInfoResponse {

	private Long eventId;

	private String title;

	private String description;

	private String whatsappGroupLink;

	private String venue;

	private Boolean active;

	private LocalDate startingDate;

	private LocalDate endingDate;

	private String profileImage;

	private UserInfoResponse createdBy;

	private LocalDateTime createdAt;

	private UserInfoResponse lastModifiedBy;

	private LocalDateTime lastModifiedAt;
}
