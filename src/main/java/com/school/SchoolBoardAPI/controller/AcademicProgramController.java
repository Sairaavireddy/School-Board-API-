package com.school.SchoolBoardAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.school.SchoolBoardAPI.service.AcademicProgramService;
import com.school.SchoolBoardAPI.userresponsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.userrequestdto.AcademicProgramRequest;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

@RestController
public class AcademicProgramController {
	@Autowired
	private AcademicProgramService academicProgramservice;
	@PostMapping("/schools/{schoolId}/academicprograms")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveAcademicProgram(@PathVariable int schoolId,@RequestBody AcademicProgramRequest academicprogramrequest ){
		return academicProgramservice.saveacademicprogram(schoolId, academicprogramrequest);
	}
	@GetMapping("/schools/{schoolId}/academicprograms")
	public List<AcademicProgramResponse> findallAcademicPrograms(@PathVariable int schoolId){
		return academicProgramservice.findallAcademicPrograms(schoolId);
	}

}
