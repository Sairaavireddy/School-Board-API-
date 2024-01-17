package com.school.SchoolBoardAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.entity.Schedule;
import com.school.SchoolBoardAPI.service.ScheduleService;
import com.school.SchoolBoardAPI.userrequestdto.ScheduleRequest;
import com.school.SchoolBoardAPI.userresponsedto.ScheduleResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

@RestController
public class ScheduleController {
	@Autowired
	private ScheduleService scheduleservice;
	@PostMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchool(@PathVariable int schoolId,@RequestBody ScheduleRequest schedulerequest) {
		return scheduleservice.saveSchool(schoolId,schedulerequest);
	}

}
