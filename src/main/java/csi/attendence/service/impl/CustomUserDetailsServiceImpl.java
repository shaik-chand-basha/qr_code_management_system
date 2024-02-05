package csi.attendence.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.entity.ImageMetadata;
import csi.attendence.entity.StudentInfo;
import csi.attendence.entity.User;
import csi.attendence.entity.UserRole;
import csi.attendence.exceptions.AlreadyExistsException;
import csi.attendence.exceptions.BadRequestException;
import csi.attendence.model.mapper.StudentMapper;
import csi.attendence.model.mapper.UserMapper;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.StudentResponse;
import csi.attendence.repository.ImageMetadataRepository;
import csi.attendence.repository.StudentRepository;
import csi.attendence.repository.UserRepository;
import csi.attendence.repository.UserroleRepository;
import csi.attendence.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private final UserRepository userRepository;

	private final StudentRepository studentRepository;

	private final ImageMetadataRepository imageRepository;

	private final UserroleRepository userroleRepository;

	private final PasswordEncoder passwordEncoder;

	@Value("${image.folder_path}")
	private String imageFolderPath;

	public static User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User loggedInUser = (User) authentication.getPrincipal();
			return loggedInUser;
		}
		return null;
	}

	public static boolean isUserAdmin(User user) {
		boolean isAdmin = false;

		if (user != null) {
			isAdmin = user.getRoles().stream().anyMatch(x -> x.getRole().equals("ROLE_ADMIN"));
		}
		return isAdmin;
	}

	public StudentResponse saveStudent(StudentRequest request,UserRequest userRequest, MultipartFile file)
			throws IllegalStateException, IOException {
		if (request == null) {
			throw new BadRequestException("studentinfo should not be empty.");
		}
		User loggedInUser = getLoggedInUser();
		boolean userAdmin = isUserAdmin(loggedInUser);
		StudentInfo requestedStudent = StudentMapper.mapToStudentInfo(request, StudentInfo.builder().build());

		checkStudentExistence(requestedStudent);
		User savedUser = saveUser(userRequest, file);
		requestedStudent.setUser(savedUser);
		if (userAdmin) {
			requestedStudent.setApproved(true);
			requestedStudent.setFkApprovedBy(loggedInUser);
		}
		StudentInfo savedStudentInfo = this.studentRepository.save(requestedStudent);
		return StudentMapper.mapToStudentResponse(savedStudentInfo, new StudentResponse());
	}

	public void checkStudentExistence(StudentInfo requestedStudent) throws AlreadyExistsException {

		boolean existsByHallticketNum = this.studentRepository
				.existsByHallticketNum(requestedStudent.getHallticketNum());
		if (existsByHallticketNum) {
			throw new AlreadyExistsException("Student", "hallticket number", requestedStudent.getHallticketNum());
		}

	}

	public User saveUser(UserRequest request, MultipartFile file) throws IllegalStateException, IOException {

		if (request == null) {
			throw new BadRequestException("userinfo should not be empty.");
		}

		checkUserExistance(request);

		User loggedInUser = getLoggedInUser();

		boolean isAdmin = false;

		if (loggedInUser != null) {
			isAdmin = loggedInUser.getRoles().stream().anyMatch(x -> x.getRole().equals("ROLE_ADMIN"));
		}
		User requestedUser = UserMapper.mapToUser(request, new User());

		requestedUser.setPassword(passwordEncoder.encode(requestedUser.getPassword()));

		List<UserRole> roles = userroleRepository.findByRoleIn(request.getRoles());
		if (roles.isEmpty()) {
			throw new RuntimeException("Atleast one role required");
		}

		ImageMetadata savedImage = saveImageMetadata(file);
		requestedUser.setFkProfile(savedImage);
		requestedUser.setRoles(roles);

		User savedUser = userRepository.save(requestedUser);
		return savedUser;
	}

	public ImageMetadata saveImageMetadata(MultipartFile imageFile) throws IllegalStateException, IOException {
		if (imageFile == null || imageFile.isEmpty()) {
			return null;
		}
		String extension = FilenameUtils.getExtension(imageFile.getOriginalFilename());

		String randomFileName = UUID.randomUUID().toString().replace("-", "");
		String fileName = "%s%l.%s".formatted(randomFileName, new Date().getTime(), extension);
		File destinationFile = Path.of(imageFolderPath, fileName).toFile();

		imageFile.transferTo(destinationFile);
		ImageMetadata imageToSave = ImageMetadata.builder().imageType(imageFile.getContentType())
				.pathToImage(destinationFile.getAbsolutePath()).build();
		ImageMetadata savedImage = imageRepository.save(imageToSave);
		return savedImage;

	}

	void checkUserExistance(UserRequest request) {

		/**
		 * check user existence 1. mobile number 2. email
		 */
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		if (Strings.isBlank(username)) {
			throw new UsernameNotFoundException("username should not be empty.");
		}
//		String usernameNotFoundMessage = String.format("user with username: %s not found.", username);
//		User user = this.userRepository.findUserByEmailOrMobileNumberOrUsername(username)
//				.orElseThrow(() -> new UsernameNotFoundException(usernameNotFoundMessage));
//		return null;
		return null;
	}

}
