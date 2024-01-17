package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.entity.Schedule;
import com.school.SchoolBoardAPI.userrequestdto.ScheduleRequest;
import com.school.SchoolBoardAPI.userresponsedto.ScheduleResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface ScheduleService {
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchool(int schoolId,ScheduleRequest schedulerequest);

}
