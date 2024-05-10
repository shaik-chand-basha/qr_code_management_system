package csi.attendence.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.entity.ImageMetadata;
import csi.attendence.exceptions.BadRequestException;
import csi.attendence.repository.ImageMetadataRepository;
import jakarta.transaction.Transactional;

@Service

@Transactional
public class ImageMetadataServiceImpl {

	public ImageMetadataServiceImpl(ImageMetadataRepository imageRepository) {
		super();
		this.imageRepository = imageRepository;
	}

	private final ImageMetadataRepository imageRepository;

	@Value("${application.image.folder_path}")
	private String imageFolderPath;

	public ImageMetadata saveImageMetadata(MultipartFile imageFile) {
		if (imageFile == null || imageFile.isEmpty()) {
			return null;
		}
		if (!isImage(imageFile)) {
			throw new BadRequestException("file %s is not an image.".formatted(imageFile.getOriginalFilename()));
		}
		Resource resource = new ClassPathResource("assets/img/db/dummy.txt");

		String extension = FilenameUtils.getExtension(imageFile.getOriginalFilename());
		String randomFileName = UUID.randomUUID().toString().replace("-", "");
		String fileName = "%s%d.%s".formatted(randomFileName, new Date().getTime(), extension);
		File destinationFile;
		try {
			destinationFile = Path.of(Paths.get(resource.getURI()).getParent().toString(), fileName).toFile();

			imageFile.transferTo(destinationFile);
		} catch (Exception e) {
			throw new RuntimeException("Unable to upload file");
		}
		ImageMetadata imageToSave = new ImageMetadata();
		imageToSave.setImageType(imageFile.getContentType());
		imageToSave.setPathToImage(destinationFile.getAbsolutePath());
		ImageMetadata savedImage = imageRepository.save(imageToSave);
		return savedImage;

	}

	public List<ImageMetadata> saveImageMetadata(List<MultipartFile> imageFiles) {
		if (imageFiles == null || imageFiles.isEmpty()) {
			return null;
		}
		ArrayList<ImageMetadata> list = new ArrayList<ImageMetadata>();

		for (MultipartFile imageFile : imageFiles) {
			if (imageFile == null || imageFile.isEmpty()) {
				continue;
			}
			if (!isImage(imageFile)) {
				throw new BadRequestException("file %s is not an image.".formatted(imageFile.getOriginalFilename()));
			}
			String extension = FilenameUtils.getExtension(imageFile.getOriginalFilename());

			String randomFileName = UUID.randomUUID().toString().replace("-", "");
			String fileName = "%s%d.%s".formatted(randomFileName, new Date().getTime(), extension);
			File destinationFile = Path.of(imageFolderPath, fileName).toFile();
			try {

				imageFile.transferTo(destinationFile);
			} catch (Exception e) {
				throw new RuntimeException("Unable to upload file");
			}
			ImageMetadata imageToSave = new ImageMetadata();
			imageToSave.setImageType(imageFile.getContentType());
			imageToSave.setPathToImage(destinationFile.getAbsolutePath());
			list.add(imageToSave);
		}

		Iterable<ImageMetadata> savedImages = imageRepository.saveAll(list);
		ArrayList<ImageMetadata> savedImageMetadatas = new ArrayList<ImageMetadata>();
		savedImages.forEach(x -> {
			savedImageMetadatas.add(x);
		});
		return savedImageMetadatas;

	}

	public static boolean isImage(MultipartFile file) {
		// Check if the content type of the file is an image type
		return file != null && MediaType.IMAGE_JPEG.isCompatibleWith(MediaType.valueOf(file.getContentType()))
				|| MediaType.IMAGE_PNG.isCompatibleWith(MediaType.valueOf(file.getContentType()))
				|| MediaType.IMAGE_GIF.isCompatibleWith(MediaType.valueOf(file.getContentType()));
	}
}
