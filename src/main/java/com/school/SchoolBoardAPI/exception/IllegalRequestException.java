package com.school.SchoolBoardAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IllegalRequestException extends RuntimeException {
	private String message;

}
