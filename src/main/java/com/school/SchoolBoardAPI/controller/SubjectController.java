package com.school.SchoolBoardAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.requestdto.SubjectRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.service.SubjectService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

@RestController
public class SubjectController {
	@Autowired
	private SubjectService subjectservice;
	@PostMapping("/academicprograms/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(@PathVariable int  programId,@RequestBody SubjectRequest subjectrequest){
		return subjectservice.addSubject(programId,subjectrequest);
	}

}
