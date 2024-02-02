package com.school.SchoolBoardAPI.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassHourDTOs 
{
	private int classHourId;
	private int subjectId;
	private int teacherId;
	private int roomNo;
	
	
}
