package com.school.SchoolBoardAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.requestdto.AcademicProgramRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.service.AcademicProgramService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

@RestController
public class AcademicProgramController {
	@Autowired
	private AcademicProgramService academicProgramservice;
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/schools/{schoolId}/academicprograms")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveAcademicProgram(@PathVariable int schoolId,@RequestBody AcademicProgramRequest academicprogramrequest ){
		return academicProgramservice.saveacademicprogram(schoolId, academicprogramrequest);
	}
	@GetMapping("/schools/{schoolId}/academicprograms")
	public List<AcademicProgramResponse> findallAcademicPrograms(@PathVariable int schoolId){
		return academicProgramservice.findallAcademicPrograms(schoolId);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
   @PutMapping("/academicprograms/{programId}/users/{userId}")
   public ResponseEntity<ResponseStructure<AcademicProgramResponse>> assignUser(@PathVariable int programId,@PathVariable int userId){
	   return academicProgramservice.assignUser(programId,userId);
   }
}
