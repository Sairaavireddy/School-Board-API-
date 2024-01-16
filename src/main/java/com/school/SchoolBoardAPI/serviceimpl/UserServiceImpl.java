package com.school.SchoolBoardAPI.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.exception.UserNotFoundException;
import com.school.SchoolBoardAPI.repository.UserRepository;
import com.school.SchoolBoardAPI.service.UserService;
import com.school.SchoolBoardAPI.userrequestdto.UserRequest;
import com.school.SchoolBoardAPI.userresponsedto.UserResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userrepository;
    @Autowired
	private ResponseStructure<UserResponse> structure;
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userrequest) {
		// Fetch existing users from the repository
        List<User> existingUsers = userrepository.findAll();
        
        // Check if the requested role is ADMIN and if an ADMIN already exists
        if (userrequest.getUserRole() == UserRole.ADMIN && isAdminExists(existingUsers)) {
            throw new IllegalArgumentException("An ADMIN user already exists.");
        }

		User user = userrepository.save(mapToUser(userrequest));
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("User saved Sucessfully");
		structure.setData(mapToUserResponse(user));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.CREATED);
	}
	
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> findUser(int userId) {
	    return userrepository.findById(userId)
	        .map(user -> {
	            ResponseStructure<UserResponse> structure = new ResponseStructure<>();
	            structure.setStatus(HttpStatus.OK.value());
	            structure.setMessage("User fetched successfully");
	            structure.setData(mapToUserResponse(user));
	            return new ResponseEntity<>(structure, HttpStatus.OK);
	        })
	        .orElseThrow(() -> new UserNotFoundException("user not found by Id"));	
	       
	}
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId) {

		return userrepository.findById(userId)
			    .map(u -> {
			        userrepository.deleteById(userId);
			        structure.setStatus(HttpStatus.OK.value());
			        structure.setMessage("user deleted successfully");
			        structure.setData(mapToUserResponse(u));

			        
			        return new ResponseEntity<>(structure, HttpStatus.OK);
			    })
			    .orElseThrow(() -> new RuntimeException("User not found"));
	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> updateUser(int userId, UserRequest userrequest) {
	    User user = userrepository.findById(userId)
	            .map(existingUser -> {
	                User updatedUser = mapToUser(userrequest);
	                updatedUser.setUserId(userId);
	                return userrepository.save(updatedUser);
	            })
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    ResponseStructure<UserResponse> structure = new ResponseStructure<>();
	    structure.setStatus(HttpStatus.OK.value());
	    structure.setMessage("User updated successfully");
	    structure.setData(mapToUserResponse(user));

	    return new ResponseEntity<>(structure, HttpStatus.OK);
	}
	
	
	private User mapToUser(UserRequest request) {
		return User.builder()
				.username(request.getUsername())
				.email(request.getEmail())
				.password(request.getPassword())
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.contactNo(request.getContactNo())
				.userRole(request.getUserRole())
				.build();
	}
	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.firstName(user.getFirstName())
                .lastName(user.getLastName())
                .contactNo(user.getContactNo())
                .email(user.getEmail())
                .userRole(user.getUserRole())
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







	

}
