package com.school.SchoolBoardAPI.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.SchoolBoardAPI.requestdto.ScheduleRequest;
import com.school.SchoolBoardAPI.responsedto.ScheduleResponse;
import com.school.SchoolBoardAPI.service.ScheduleService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

@RestController
public class ScheduleController {
	@Autowired
	private ScheduleService scheduleservice;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(@PathVariable int schoolId,@RequestBody ScheduleRequest schedulerequest) {
		return scheduleservice.saveSchedule(schoolId,schedulerequest);
	}
	@GetMapping("/schools/{schoolId}/schedules")
	public  ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(@PathVariable int schoolId){
		return scheduleservice.findSchedule(schoolId);
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/schedules/{scheduleId}/update")
	public  ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(@PathVariable int scheduleId,@RequestBody ScheduleRequest schedulerequest){
		return scheduleservice.updateSchedule(scheduleId,schedulerequest);
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/schedules/{scheduleId}")
	public void deleteSchedule(@PathVariable int scheduleId) {
		 scheduleservice.deleteSchedule(scheduleId);
	}

}
