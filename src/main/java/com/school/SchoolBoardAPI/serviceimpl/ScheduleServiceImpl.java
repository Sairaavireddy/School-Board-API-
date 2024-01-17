package com.school.SchoolBoardAPI.serviceimpl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.Schedule;
import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.repository.ScheduleRepository;
import com.school.SchoolBoardAPI.repository.SchoolRepository;
import com.school.SchoolBoardAPI.service.ScheduleService;
import com.school.SchoolBoardAPI.userrequestdto.ScheduleRequest;
import com.school.SchoolBoardAPI.userresponsedto.ScheduleResponse;
import com.school.SchoolBoardAPI.userresponsedto.SchoolResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
@Service
public class ScheduleServiceImpl implements ScheduleService{

	@Autowired
	private SchoolRepository schoolrepository;
	@Autowired
	private ScheduleRepository schedulerepository;
	@Autowired
	private ResponseStructure<ScheduleResponse> structure;
	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchool(int schoolId,ScheduleRequest schedulerequest) {
		return schoolrepository.findById(schoolId).map(s->{
			if(s.getSchedule() == null) {
			Schedule schedule = mapToSchedule(schedulerequest);
			schedule= schedulerepository.save(schedule);
			s.setSchedule(schedule);
			schoolrepository.save(s);
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("schedule object created Sucessfully");
			structure.setData(mapToScheduleResponse(schedule));
			return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.CREATED);
			}else throw new IllegalRequestException("Schedule object is alredy present");
		}).orElseThrow(()->new IllegalRequestException("School has only one school id that is of ADMINS"));
			
	}
	private ScheduleResponse mapToScheduleResponse(Schedule schedule) {
		return ScheduleResponse.builder()
				.opensAt(schedule.getOpensAt())
				.closesAt(schedule.getClosesAt())
				.classHoursPerDay(schedule.getClassHoursPerDay())
				.classHoursLengthInMinutes(schedule.getClassHoursLengthInMinutes())
				.breakLengthInMinutes(schedule.getBreakLengthInMinutes())
				.lunchTime(schedule.getLunchTime())
				.lunchLengthInMinutes(schedule.getLunchLengthInMinutes())
				.build();

	}
	private Schedule mapToSchedule(ScheduleRequest scheduleRequest) {
	    return Schedule.builder()
	            .opensAt(scheduleRequest.getOpensAt())
	            .closesAt(scheduleRequest.getClosesAt())
	            .classHoursPerDay(scheduleRequest.getClassHoursPerDay())
	            .classHoursLengthInMinutes(Duration.ofMinutes(scheduleRequest.getClassHoursLengthInMinutes()))
	            .breakTime(scheduleRequest.getBreakTime())
	            .breakLengthInMinutes(Duration.ofMinutes(scheduleRequest.getBreakLengthInMinutes()))
	            .lunchTime(scheduleRequest.getLunchTime())
	            .lunchLengthInMinutes(Duration.ofMinutes(scheduleRequest.getLunchLengthInMinutes()))
	            .build();
	}

}
