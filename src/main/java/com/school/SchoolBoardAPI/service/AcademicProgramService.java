package com.school.SchoolBoardAPI.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.userrequestdto.AcademicProgramRequest;
import com.school.SchoolBoardAPI.userresponsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface AcademicProgramService {
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveacademicprogram(int schoolId,AcademicProgramRequest academicprogramrequest);

//	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> findallAcademicProgram(int schoolId);

	List<AcademicProgramResponse> findallAcademicPrograms(int schoolId);



}
