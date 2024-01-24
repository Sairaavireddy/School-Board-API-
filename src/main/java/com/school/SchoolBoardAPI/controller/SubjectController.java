package com.school.SchoolBoardAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.entity.Subject;
import com.school.SchoolBoardAPI.requestdto.SubjectRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.responsedto.SubjectResponse;
import com.school.SchoolBoardAPI.responsedto.UserResponse;
import com.school.SchoolBoardAPI.service.SubjectService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

import lombok.Getter;

@RestController
public class SubjectController {
	@Autowired
	private SubjectService subjectservice;
	@PostMapping("/academicprograms/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(@PathVariable int  programId,@RequestBody SubjectRequest subjectrequest){
		return subjectservice.addSubject(programId,subjectrequest);
	}
	@PutMapping("/academicprograms/{programId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubject(@PathVariable  int programId,@RequestBody SubjectRequest subjectRequest){
		return subjectservice.addSubject(programId, subjectRequest);
	}
	@GetMapping("/subjects")
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects(){
		return subjectservice.findAllSubjects();
	}
//	@PutMapping("/subjects/{subjectId}/users/{userId}")
//	public ResponseEntity<ResponseStructure<UserResponse>> assignSubjectToUser(@PathVariable int subjectId,@PathVariable int userId){
//		return subjectservice.assignSubjectToUser(subjectId,userId);
//	}

}
