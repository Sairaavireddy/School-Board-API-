package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.school.SchoolBoardAPI.requestdto.SubjectRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface SubjectService {
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(int  programId,SubjectRequest subjectrequest);

}
