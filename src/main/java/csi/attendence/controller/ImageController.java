package csi.attendence.controller;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.entity.ImageMetadata;
import csi.attendence.model.response.ApiResponse;
import csi.attendence.service.impl.ImageMetadataServiceImpl;
import csi.attendence.utils.UrlUtils;

@RestController
@RequestMapping(ImageController.BASE_URL)
@Validated

public class ImageController {

	public static final String BASE_URL = "/api/v1/image";

	private final ImageMetadataServiceImpl imageMetadataService;

	@PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> uploadImagesToThisEvent(@RequestParam("image") MultipartFile image)
			throws IOException {
		ImageMetadata imageMetadata = this.imageMetadataService.saveImageMetadata(image);
		ApiResponse apiResponse = ApiResponse.builder().status(HttpStatus.CREATED)
				.message(imageMetadata.getId().toString()).build();

		return ResponseEntity.created(URI.create(UrlUtils.pathToUrl(imageMetadata.getPathToImage()))).body(apiResponse);
	}

	public ImageController(ImageMetadataServiceImpl imageMetadataService) {
		super();
		this.imageMetadataService = imageMetadataService;
	}
}
