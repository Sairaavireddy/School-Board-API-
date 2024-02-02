package com.school.SchoolBoardAPI.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.requestdto.ClassHourDTOs;
import com.school.SchoolBoardAPI.responsedto.ClassHourResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<String> generateClassHourForAcademicProgram(int programId);
	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(List<ClassHourDTOs> classHourDtoList);

	ResponseEntity<String> AutoRepeatNextWeekClassHours();








}
