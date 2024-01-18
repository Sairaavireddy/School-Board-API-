package com.school.SchoolBoardAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("/users")
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@Valid @RequestBody UserRequest userrequest) {
		return userservice.saveUser(userrequest);	
	}
	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> findUser(@PathVariable int userId) {
		return userservice.findUser(userId);
	}
	@PutMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> updateUser(@PathVariable int userId,@Valid @RequestBody UserRequest userrequest) {
		return userservice.updateUser(userId,userrequest);
 
	}
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(@PathVariable int userId) {
		return userservice.deleteUser(userId);
	}

}
