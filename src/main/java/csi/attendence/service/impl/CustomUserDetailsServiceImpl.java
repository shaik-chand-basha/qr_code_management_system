package csi.attendence.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import csi.attendence.entity.ImageMetadata;
import csi.attendence.entity.StudentInfo;
import csi.attendence.entity.User;
import csi.attendence.entity.UserRole;
import csi.attendence.enums.LoginType;
import csi.attendence.exceptions.AlreadyExistsException;
import csi.attendence.exceptions.BadRequestException;
import csi.attendence.listener.events.OnRegisterUserEvent;
import csi.attendence.model.mapper.StudentMapper;
import csi.attendence.model.mapper.UserMapper;
import csi.attendence.model.request.StudentRequest;
import csi.attendence.model.request.UserRequest;
import csi.attendence.model.response.ApiResponse;
import csi.attendence.model.response.StudentResponse;
import csi.attendence.repository.ImageMetadataRepository;
import csi.attendence.repository.StudentRepository;
import csi.attendence.repository.UserRepository;
import csi.attendence.repository.UserroleRepository;
import csi.attendence.service.CustomUserDetailsService;
import csi.attendence.utils.AuthenticationUtils;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
@Transactional
@Getter
@Setter
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private final UserRepository userRepository;

	private final StudentRepository studentRepository;

	private final ImageMetadataRepository imageRepository;

	private final UserroleRepository userroleRepository;

	private final PasswordEncoder passwordEncoder;

	private final EntityManager em;

	private final ApplicationEventPublisher applicationEventPublisher;

	@Value("${application.image.folder_path}")
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
	public StudentResponse saveStudent(StudentRequest request, String siteUrl) {
		if (request == null) {
			throw new BadRequestException("studentinfo should not be empty.");
		}
		User loggedInUser = AuthenticationUtils.getLoggedInUser();
		boolean userAdmin = AuthenticationUtils.isUserAdmin(loggedInUser);
		StudentInfo requestedStudent = StudentMapper.mapToStudentInfo(request, new StudentInfo());
		UserRequest userRequest = request.getUserInfo();
		checkStudentExistence(requestedStudent);
		User savedUser = saveUser(userRequest, siteUrl);
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
	public User saveUser(UserRequest request, String siteURL) {

		if (request == null) {
			throw new BadRequestException("userinfo should not be empty.");
		}

		checkUserExistance(request);

		User loggedInUser = AuthenticationUtils.getLoggedInUser();

		boolean userAdmin = AuthenticationUtils.isUserAdmin(loggedInUser);

		User requestedUser = UserMapper.mapToUser(request, new User());
		requestedUser.setActive(true);
		requestedUser.setPassword(passwordEncoder.encode(requestedUser.getPassword()));

		List<UserRole> roles = userroleRepository.findByRoleIn(request.getRoles());
		if (roles.isEmpty()) {
			throw new RuntimeException("Atleast one role required");
		}

		requestedUser.setRoles(roles);

		User savedUser = userRepository.save(requestedUser);
		this.applicationEventPublisher.publishEvent(new OnRegisterUserEvent(savedUser, siteURL));
		return savedUser;
	}

	@Override
	public ApiResponse updateProfileImage(MultipartFile file) {
		User loggedInUser = AuthenticationUtils.getLoggedInUser();
		if(loggedInUser == null) {
			throw new BadCredentialsException("Authentication required");
		}
		User user = this.userRepository.findById(loggedInUser.getUserId()).orElse(null);

		try {
			ImageMetadata imageMetadata = saveImageMetadata(file);
			user.setFkProfile(imageMetadata);
			User savedUser = this.userRepository.save(user);
			ApiResponse apiResponse = ApiResponse.builder().message(imageMetadata.getPathToImage())
					.status(HttpStatus.OK).build();
			return apiResponse;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

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

	@Override
	public User findUserByCredentials(String authorizationHeader, LoginType loginType) {
		if (Strings.isBlank(authorizationHeader)) {
			throw new BadRequestException();
		}

		String decodedHeader = Base64.getDecoder().decode(authorizationHeader).toString().substring(7);
		User user = null;
		String username = decodedHeader.split(":")[0];
		String password = decodedHeader.split(":")[1];
		if (LoginType.EMAIL.equals(loginType)) {
			user = this.userRepository.findByEmailAndActive(username)
					.orElseThrow(() -> new UsernameNotFoundException("username not found"));
		} else if (LoginType.HALLTICKET_NUMBER.equals(loginType)) {
			user = this.userRepository.findByHallticketNumber(Long.parseLong(username))
					.orElseThrow(() -> new UsernameNotFoundException("username not found"));
		} else if (LoginType.MOBILE_NUMBER.equals(loginType)) {
			user = this.userRepository.findByMobileNumberAndActiveTrue(username)
					.orElseThrow(() -> new UsernameNotFoundException("username not found"));
		} else {
			throw new BadRequestException();
		}
		if (user == null) {
			throw new UsernameNotFoundException("username not found");
		}
		boolean matches = passwordEncoder.matches(password, user.getPassword());
		if (matches) {
			return user;
		}
		throw new BadRequestException();
	}

	public User findUserByCredentials(String username) {
		if (Strings.isBlank(username)) {
			throw new UsernameNotFoundException("username should not be empty.");
		}
		User user = null;
		user = this.userRepository.findByEmailAndActive(username).orElse(null);
		if (user == null) {
			user = this.userRepository.findByMobileNumberAndActiveTrue(username).orElse(null);
		}
		if (user == null) {

			user = this.userRepository.findByHallticketNumber(Long.parseLong(username)).orElse(null);
		}

		return user;

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
		return findUserByCredentials(username);

	}

}
