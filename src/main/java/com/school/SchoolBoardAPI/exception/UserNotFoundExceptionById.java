package com.school.SchoolBoardAPI.exception;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserNotFoundExceptionById extends RuntimeException {
	
	private String message;

}

