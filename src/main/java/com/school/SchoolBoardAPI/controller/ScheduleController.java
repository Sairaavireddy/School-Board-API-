package com.school.SchoolBoardAPI.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.school.SchoolBoardAPI.service.ScheduleService;
import com.school.SchoolBoardAPI.userrequestdto.ScheduleRequest;
import com.school.SchoolBoardAPI.userresponsedto.ScheduleResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

@RestController
public class ScheduleController {
	@Autowired
	private ScheduleService scheduleservice;
	
	@PostMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(@PathVariable int schoolId,@RequestBody ScheduleRequest schedulerequest) {
		return scheduleservice.saveSchedule(schoolId,schedulerequest);
	}
	@GetMapping("/schools/{schoolId}/schedules")
	public  ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(@PathVariable int schoolId){
		return scheduleservice.findSchedule(schoolId);
	}
	@PutMapping("/schedules/{scheduleId}")
	public  ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(@PathVariable int scheduleId,@RequestBody ScheduleRequest schedulerequest){
		return scheduleservice.updateSchedule(scheduleId,schedulerequest);
	}
	@DeleteMapping("/schedules/{scheduleId}")
	public void deleteSchedule(@PathVariable int scheduleId) {
		 scheduleservice.deleteSchedule(scheduleId);
	}

}
