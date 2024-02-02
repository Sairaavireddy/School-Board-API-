package com.school.SchoolBoardAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.requestdto.ClassHourDTOs;
import com.school.SchoolBoardAPI.requestdto.ClassHourRequest;
import com.school.SchoolBoardAPI.responsedto.ClassHourResponse;
import com.school.SchoolBoardAPI.service.ClassHourService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;


@RestController
public class ClassHourController {
	@Autowired
	private ClassHourService classHourService;

	@PostMapping("/academic-program/{programId}/class-hours")
	public ResponseEntity<String> generateClassHourForAcademicProgram(@PathVariable int programId)
	{
		return classHourService.generateClassHourForAcademicProgram(programId);
	}
	@PutMapping("/class-hours")
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHours(@RequestBody List<ClassHourDTOs> classHourDtoList){
		return classHourService.updateClassHour(classHourDtoList);
	}
	@PostMapping("/nextweekclasshours")
	public  ResponseEntity<String> AutoRepeatNextWeekClassHours(){
		return classHourService.AutoRepeatNextWeekClassHours();
	}

}
