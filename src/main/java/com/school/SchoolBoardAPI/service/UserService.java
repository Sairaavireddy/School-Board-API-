package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.requestdto.UserRequest;
import com.school.SchoolBoardAPI.responsedto.UserResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

import jakarta.validation.Valid;

public interface UserService {
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(@Valid UserRequest userrequest);
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUsers(UserRequest userrequest);
    public ResponseEntity<ResponseStructure<UserResponse>> findUser(int userId);
    public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);
    public ResponseEntity<ResponseStructure<UserResponse>> updateUser(int userId,UserRequest userrequest);
	

}
