package com.school.SchoolBoardAPI.serviceimpl;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.school.SchoolBoardAPI.entity.Schedule;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.repository.ScheduleRepository;
import com.school.SchoolBoardAPI.repository.SchoolRepository;
import com.school.SchoolBoardAPI.requestdto.ScheduleRequest;
import com.school.SchoolBoardAPI.responsedto.ScheduleResponse;
import com.school.SchoolBoardAPI.service.ScheduleService;
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
	public ResponseEntity<ResponseStructure<ScheduleResponse>> saveSchedule(int schoolId,ScheduleRequest schedulerequest) {
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
	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(int schoolId) {
		return schoolrepository.findById(schoolId)
				.map(school -> {
					Schedule schedule = school.getSchedule();
					if (schedule != null) {	
						structure.setStatus(HttpStatus.OK.value());
						structure.setMessage("Schedule data fetched successfully");
						structure.setData(mapToScheduleResponse(schedule));
						return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.OK);
					} else {
						throw new IllegalRequestException("Schedule not found for schoolID");
					}
				})
				.orElseThrow(() -> new IllegalRequestException("School not found by Id"));
	}
	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest scheduleRequest) {
	    return schedulerepository.findById(scheduleId)
	            .map(existingSchedule -> {
	                    Schedule updatedSchedule = mapToSchedule(scheduleRequest);
	                    updatedSchedule.setScheduleId(scheduleId);
	                    Schedule savedSchedule = schedulerepository.save(updatedSchedule);

	                   
	                    structure.setStatus(HttpStatus.OK.value());
	                    structure.setMessage("Schedule updated successfully");
	                    structure.setData(mapToScheduleResponse(savedSchedule));

	                    return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.OK);
	            })
	            .orElseThrow(() -> new IllegalRequestException("No Schedule found to upadate"));
	}
	@Override
	public void deleteSchedule(int scheduleId) {
	      schedulerepository.deleteById(scheduleId);
	}
	
	
	
	
	private ScheduleResponse mapToScheduleResponse(Schedule schedule) {
		return ScheduleResponse.builder()
				.scheduleId(schedule.getScheduleId())
				.opensAt(schedule.getOpensAt())
				.closesAt(schedule.getClosesAt())
				.classHoursPerDay(schedule.getClassHoursPerDay())
				.classHoursLengthInMinutes((int)schedule.getClassHoursLengthInMinutes().toMinutes())
				.breakTime(schedule.getBreakTime())
				.breakLengthInMinutes((int)schedule.getBreakLengthInMinutes().toMinutes())
				.lunchTime(schedule.getLunchTime())
				.lunchLengthInMinutes((int) schedule.getLunchLengthInMinutes().toMinutes())
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

