package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.userrequestdto.ScheduleRequest;
import com.school.SchoolBoardAPI.userresponsedto.ScheduleResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface ScheduleService {
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(int schoolId,ScheduleRequest schedulerequest);

	public ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(int schoolId);



	ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest scheduleRequest);

	public void deleteSchedule(int scheduleId);

}
