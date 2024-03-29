package com.school.SchoolBoardAPI.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.exception.UserNotFoundExceptionById;
import com.school.SchoolBoardAPI.repository.AcademicProgramRepository;
import com.school.SchoolBoardAPI.repository.ClassHourRepository;
import com.school.SchoolBoardAPI.repository.UserRepository;
import com.school.SchoolBoardAPI.requestdto.UserRequest;
import com.school.SchoolBoardAPI.responsedto.UserResponse;
import com.school.SchoolBoardAPI.service.UserService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private ResponseStructure<UserResponse> structure;

	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private AcademicProgramRepository academicProgramRepository;
	@Autowired
	private ClassHourRepository classRepository;



	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(@Valid UserRequest userrequest) {
		if(userrequest.getUserRole()==UserRole.ADMIN) {
			// Fetch existing users from the repository
			List<User> existingUsers = userrepository.findAll();

			// Check if the requested role is ADMIN and if an ADMIN already exists
			if (userrequest.getUserRole() == UserRole.ADMIN && isAdminExists(existingUsers)) {
				throw new IllegalArgumentException("An ADMIN user already exists.");
			}
		}
		else {
			throw new IllegalArgumentException("only Admin can register");
		}
		User user = userrepository.save(mapToUser(userrequest,false));
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("User saved Sucessfully");
		structure.setData(mapToUserResponse(user,false));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.CREATED);

	}
	//	int adminId = userrepository.findByUserRole(UserRole.ADMIN);
	//	int adminSchoolId = userrepository.findByUserId(adminId);


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUsers(UserRequest userrequest) {
		// Fetch existing users from the repository
		List<User> existingUsers = userrepository.findAll();

		// Check if the requested role is ADMIN and if an ADMIN already exists
		if (userrequest.getUserRole() == UserRole.ADMIN && isAdminExists(existingUsers)) {
			throw new IllegalArgumentException("An ADMIN user already exists.");
		}



		User user = userrepository.save(mapToUser(userrequest,false));

		if (user.getUserRole() == UserRole.TEACHER || user.getUserRole() == UserRole.STUDENT) {
			mapUserToAdminSchool(user);
		}
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("User saved Sucessfully");
		structure.setData(mapToUserResponse(user,false));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.CREATED);
	}


	private void mapUserToAdminSchool(User user) {
		// Find the admin user
		User admin =userrepository.findUserByUserRole(UserRole.ADMIN)
				.orElseThrow(() -> new IllegalStateException("Admin user not found."));

		// Map the user to the same school as the admin
		user.setSchool(admin.getSchool());
		userrepository.save(user);

	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> findUser(int userId) {
		return userrepository.findById(userId)
				.map(user -> {
					ResponseStructure<UserResponse> structure = new ResponseStructure<>();
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("User fetched successfully");
					structure.setData(mapToUserResponse(user,false));
					return new ResponseEntity<>(structure, HttpStatus.OK);
				})
				.orElseThrow(() -> new UserNotFoundExceptionById("user not found by Id"));	

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId) {

		return userrepository.findById(userId)
				.map(user -> {
					if(user.getUserRole().equals(UserRole.ADMIN))
						throw new IllegalRequestException("Admin cannot be deleted");

					user.setDeleted(true);
					//					userrepository.deleteById(userId);
					userrepository.save(user);
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("user deleted successfully");
					structure.setData(mapToUserResponse(user,true));
					//                    System.out.println(user.isDeleted);

					return new ResponseEntity<>(structure, HttpStatus.OK);
				})
				.orElseThrow(() -> new UserNotFoundExceptionById("User not found by id"));
	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> updateUser(int userId, UserRequest userrequest) {
		User user = userrepository.findById(userId)
				.map(existingUser -> {
					User updatedUser = mapToUser(userrequest,false);
					updatedUser.setUserId(userId);
					return userrepository.save(updatedUser);
				})
				.orElseThrow(() -> new UserNotFoundExceptionById("User not found by id"));

		ResponseStructure<UserResponse> structure = new ResponseStructure<>();
		structure.setStatus(HttpStatus.OK.value());
		structure.setMessage("User updated successfully");
		structure.setData(mapToUserResponse(user,false));

		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.OK);
	}


	private User mapToUser(UserRequest request, boolean isDeleted) {
		return User.builder()
				.username(request.getUsername())
				.email(request.getEmail())
				.password(encoder.encode(request.getPassword()))
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.contactNo(request.getContactNo())
				.userRole(request.getUserRole())
				.isDeleted(isDeleted)
				.build();
	}
	public static UserResponse mapToUserResponse(User user, boolean isDeleted) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.contactNo(user.getContactNo())
				.email(user.getEmail())
				.userRole(user.getUserRole())
				.isDeleted(isDeleted) // Use the parameter here
				.build();
	}
	private boolean isAdminExists(List<User> users) {
		for (User user : users) {
			if (user.getUserRole() == UserRole.ADMIN) {
				return true;
			}
		}
		return false;
	}
	public void deleteUsers() {
		List<User> usersToDelete = new ArrayList<User>();

		userrepository.findByIsDeleted(true)
		.forEach(user ->
		{
			user.getClassHourlist().forEach(classHour -> classHour.setUser(null));
			classRepository.saveAll(user.getClassHourlist());

			user.getListAcademicPrograms().forEach(academicProgram -> academicProgram.setUserlist(null));
			academicProgramRepository.saveAll(user.getListAcademicPrograms());

			usersToDelete.add(user);
		});
		userrepository.deleteAll(usersToDelete);
	}

}
