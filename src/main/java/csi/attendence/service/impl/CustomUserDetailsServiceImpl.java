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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import csi.attendence.utils.AuthenticationUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private final UserRepository userRepository;

	private final StudentRepository studentRepository;

	private final ImageMetadataRepository imageRepository;

	private final UserroleRepository userroleRepository;

	private final PasswordEncoder passwordEncoder;

	private final EntityManager em;

	@Value("${image.folder_path}")
	private String imageFolderPath;

	@Override
	public StudentResponse findStudent(Long id) {
		if (id == null) {
			throw new BadRequestException("StudentId should not be empty!");
		}
		User loggedInUser = AuthenticationUtils.getLoggedInUser();
		boolean userAdmin = AuthenticationUtils.isUserAdmin(loggedInUser);
//		if (loggedInUser == null) {
//			return null;
//		}
		StudentInfo studentInfo = this.studentRepository.findById(id).orElse(null);
		return StudentMapper.mapToStudentResponse(studentInfo, new StudentResponse());
	}

	@Override
	public StudentResponse saveStudent(StudentRequest request) {
		if (request == null) {
			throw new BadRequestException("studentinfo should not be empty.");
		}
		User loggedInUser = AuthenticationUtils.getLoggedInUser();
		boolean userAdmin = AuthenticationUtils.isUserAdmin(loggedInUser);
		StudentInfo requestedStudent = StudentMapper.mapToStudentInfo(request, new StudentInfo());
		UserRequest userRequest = request.getUserInfo();
		checkStudentExistence(requestedStudent);
		User savedUser = saveUser(userRequest);
		requestedStudent.setUser(savedUser);
		if (userAdmin) {
			requestedStudent.setApproved(true);
			requestedStudent.setFkApprovedBy(loggedInUser);
		} else {
			requestedStudent.setApproved(false);
		}
		StudentInfo savedStudentInfo = this.studentRepository.saveAndFlush(requestedStudent);
		StudentInfo studentInfo = this.studentRepository.findById(savedStudentInfo.getId()).orElse(null);
		return StudentMapper.mapToStudentResponse(studentInfo, new StudentResponse());
	}

	public void checkStudentExistence(StudentInfo requestedStudent) throws AlreadyExistsException {

		boolean existsByHallticketNum = this.studentRepository
				.existsByHallticketNum(requestedStudent.getHallticketNum());
		if (existsByHallticketNum) {
			throw new AlreadyExistsException("Student", "hallticket number", requestedStudent.getHallticketNum());
		}

		boolean existsByCsiId = this.studentRepository.existsByCsiId(requestedStudent.getCsiId());
		if (existsByCsiId) {
			throw new AlreadyExistsException("Student", "CSI Id", requestedStudent.getCsiId());
		}

	}

	@Override
	public User saveUser(UserRequest request) {

		if (request == null) {
			throw new BadRequestException("userinfo should not be empty.");
		}

		checkUserExistance(request);

		User loggedInUser = AuthenticationUtils.getLoggedInUser();

		boolean userAdmin = AuthenticationUtils.isUserAdmin(loggedInUser);

		User requestedUser = UserMapper.mapToUser(request, new User());

		requestedUser.setPassword(passwordEncoder.encode(requestedUser.getPassword()));

		List<UserRole> roles = userroleRepository.findByRoleIn(request.getRoles());
		if (roles.isEmpty()) {
			throw new RuntimeException("Atleast one role required");
		}

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
		ImageMetadata imageToSave = new ImageMetadata();
		imageToSave.setImageType(imageFile.getContentType());
		imageToSave.setPathToImage(destinationFile.getAbsolutePath());
		ImageMetadata savedImage = imageRepository.save(imageToSave);
		return savedImage;

	}

	void checkUserExistance(UserRequest request) {

		boolean existsByEmail = this.userRepository.existsByEmail(request.getEmail());

		if (existsByEmail) {
			throw new AlreadyExistsException("User", "email", request.getEmail());
		}

		boolean existsByMobileNumber = this.userRepository.existsByMobileNumber(request.getMobileNumber());

		if (existsByMobileNumber) {
			throw new AlreadyExistsException("User", "mobileNumber", request.getMobileNumber());
		}
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
