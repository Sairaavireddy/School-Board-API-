package com.school.SchoolBoardAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.service.SchoolService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
@RestController
@RequestMapping("/schools")
public class SchoolController {
	@Autowired
	private SchoolService schoolservice;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<School>> saveSchool(@RequestBody School school) {
		return  schoolservice.SaveSchool(school);
	
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
//	@ResponseBody
//	@RequestMapping(value="deleteschool",method=RequestMethod.POST)
//	public String deleteSchool() {
//		return schoolservice.deleteSchool();
//	
//	}
}
