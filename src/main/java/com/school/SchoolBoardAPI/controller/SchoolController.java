package com.school.SchoolBoardAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.requestdto.SchoolRequest;
import com.school.SchoolBoardAPI.responsedto.SchoolResponse;
import com.school.SchoolBoardAPI.service.SchoolService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
@RestController
public class SchoolController {
	@Autowired
	private SchoolService schoolservice;
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/users/schools")
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(@RequestBody SchoolRequest schoolrequest) {
		return  schoolservice.SaveSchool( schoolrequest);
	
	}
//	@ResponseBody
//	@RequestMapping(value="/findschool",method=RequestMethod.GET)
//	public Optional<School> findschool(@RequestParam int schoolId) {
//		return schoolservice.findSchool(schoolId);
//	
//	}
//	@ResponseBody
//	@RequestMapping(value="updateschool",method=RequestMethod.GET)
//	public School updatedschool(@RequestParam int  schoolId,@RequestBody School updatedschool) {
//		return schoolservice.updatedSchool(schoolId,updatedschool);
//	
//	}

	@DeleteMapping("/schools/{schoolId}")
	public void  deleteSchool(@PathVariable int schoolId) {
		schoolservice.deleteSchool(schoolId);
	
	}
}
