package csi.attendence.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.entity.ImageMetadata;
import csi.attendence.repository.ImageMetadataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageMetadataServiceImpl {

	private final ImageMetadataRepository imageRepository;

	@Value("${application.image.folder_path}")
	private String imageFolderPath;

	public ImageMetadata saveImageMetadata(MultipartFile imageFile) throws IllegalStateException, IOException {
		if (imageFile == null || imageFile.isEmpty()) {
			return null;
		}
		String extension = FilenameUtils.getExtension(imageFile.getOriginalFilename());

		String randomFileName = UUID.randomUUID().toString().replace("-", "");
		String fileName = "%s%d.%s".formatted(randomFileName, new Date().getTime(), extension);
		File destinationFile = Path.of(imageFolderPath, fileName).toFile();

		imageFile.transferTo(destinationFile);
		ImageMetadata imageToSave = new ImageMetadata();
		imageToSave.setImageType(imageFile.getContentType());
		imageToSave.setPathToImage(destinationFile.getAbsolutePath());
		ImageMetadata savedImage = imageRepository.save(imageToSave);
		return savedImage;

	}
}
