package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.userrequestdto.AcademicProgramRequest;
import com.school.SchoolBoardAPI.userresponsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface AcademicProgramService {
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveacademicprogram(int schoolId,AcademicProgramRequest academicprogramrequest);

}
