package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.userrequestdto.UserRequest;
import com.school.SchoolBoardAPI.userresponsedto.UserResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface UserService {
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userrequest);
    public ResponseEntity<ResponseStructure<UserResponse>> findUser(int userId);
    public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);
    public ResponseEntity<ResponseStructure<UserResponse>> updateUser(int userId,UserRequest userrequest);

}
