package com.school.SchoolBoardAPI.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.requestdto.AcademicProgramRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.responsedto.UserResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface AcademicProgramService {
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveacademicprogram(int schoolId,AcademicProgramRequest academicprogramrequest);

//	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> findallAcademicProgram(int schoolId);

	List<AcademicProgramResponse> findallAcademicPrograms(int schoolId);

	 public ResponseEntity<ResponseStructure<AcademicProgramResponse>> assignUser(int programId,int userId);

	

//	ResponseEntity<ResponseStructure<List<User>>> findUsersInProgram(int programId, String userRole);

	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteprogram(int programId);

	

}
