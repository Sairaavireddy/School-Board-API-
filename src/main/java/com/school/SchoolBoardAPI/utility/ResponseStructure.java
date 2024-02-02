package com.school.SchoolBoardAPI.utility;

import java.util.List;

import org.springframework.stereotype.Component;

import com.school.SchoolBoardAPI.responsedto.ClassHourResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseStructure<T> {
	
		private int status;
		private String message;
		private T data;
	
}
