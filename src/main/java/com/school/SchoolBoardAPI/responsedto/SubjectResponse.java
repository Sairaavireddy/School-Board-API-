package com.school.SchoolBoardAPI.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SubjectResponse {
	private int subjectId;

	private String subjectName;
	
	
}
