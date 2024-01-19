package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.entity.Subject;
import com.school.SchoolBoardAPI.requestdto.SubjectRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface SubjectService {
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(int  programId,SubjectRequest subjectrequest);
//
//	ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubject(int programId,
//			SubjectRequest subjectRequest);

}
