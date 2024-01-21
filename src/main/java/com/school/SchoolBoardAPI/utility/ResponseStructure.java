package com.school.SchoolBoardAPI.utility;

import java.util.List;

import org.springframework.stereotype.Component;

import com.school.SchoolBoardAPI.responsedto.SubjectResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ResponseStructure<T> {
	
		private int status;
		private String message;
		private T data;
}
