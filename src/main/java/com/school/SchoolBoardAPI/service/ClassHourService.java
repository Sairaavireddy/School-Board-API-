package com.school.SchoolBoardAPI.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.school.SchoolBoardAPI.requestdto.ClassHourDTOs;
import com.school.SchoolBoardAPI.requestdto.ExcelRequestDto;
import com.school.SchoolBoardAPI.responsedto.ClassHourResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<String> generateClassHourForAcademicProgram(int programId);
	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(List<ClassHourDTOs> classHourDtoList);

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> AutoRepeatNextWeekClassHours();

	ResponseEntity<String> excelSheet(int programId, ExcelRequestDto excelRequestDto) throws Exception, Exception;
	ResponseEntity<?> WriteToexcelSheet(int programId, LocalDate fromDate, LocalDate toDate, MultipartFile file) throws Exception;
	
	







}
