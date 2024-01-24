package com.school.SchoolBoardAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.requestdto.UserRequest;
import com.school.SchoolBoardAPI.responsedto.UserResponse;
import com.school.SchoolBoardAPI.service.UserService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

import jakarta.validation.Valid;




@RestController
@RequestMapping
public class UserController {
	@Autowired
	private UserService userservice;
	
	@PostMapping("/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(@Valid @RequestBody UserRequest userrequest) {
		return userservice.registerAdmin(userrequest);	
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/users")
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUsers(@Valid @RequestBody UserRequest userrequest) {
		return userservice.addOtherUsers(userrequest);	
	}
	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> findUser(@PathVariable int userId) {
		return userservice.findUser(userId);
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/update/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> updateUser(@PathVariable int userId,@Valid @RequestBody UserRequest userrequest) {
		return userservice.updateUser(userId,userrequest);
 
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(@PathVariable int userId) {
		return userservice.deleteUser(userId);
	}

}
