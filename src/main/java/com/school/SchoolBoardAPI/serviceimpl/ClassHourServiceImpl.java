package com.school.SchoolBoardAPI.serviceimpl;



import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.ClassHour;
import com.school.SchoolBoardAPI.entity.Schedule;
import com.school.SchoolBoardAPI.entity.Subject;
import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.enums.classStatus;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.repository.AcademicProgramRepository;
import com.school.SchoolBoardAPI.repository.ClassHourRepository;
import com.school.SchoolBoardAPI.repository.SubjectRepository;
import com.school.SchoolBoardAPI.repository.UserRepository;
import com.school.SchoolBoardAPI.requestdto.ClassHourDTOs;
import com.school.SchoolBoardAPI.responsedto.ClassHourResponse;
import com.school.SchoolBoardAPI.service.ClassHourService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;



@Service
public class ClassHourServiceImpl implements ClassHourService {

	@Autowired
	private AcademicProgramRepository academicProgramRepository;
	@Autowired
	private ClassHourRepository classHourRepository;
	@Autowired
	private UserRepository userrepository;

	@Autowired
	private SubjectRepository subjectrepository;
	ResponseStructure<List<ClassHourResponse>> responseStructure = new ResponseStructure<List<ClassHourResponse>>();	
	@Override
	public ResponseEntity<String> generateClassHourForAcademicProgram(int programId) {
		return academicProgramRepository.findById(programId).map(academicProgram->{
			Schedule scheduleld = academicProgram.getSchool().getSchedule();
			if(scheduleld != null) {
				int classHoursPerDay = scheduleld.getClassHoursPerDay();
				int classHourLength = (int)scheduleld.getClassHoursLengthInMinutes().toMinutes();

				LocalDateTime currentTime = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).with(scheduleld.getOpensAt());
				// Pre-calculate time renges for clarity
				LocalTime lunchTimeStart = scheduleld.getLunchTime();
				LocalTime lunchTimeEnd = lunchTimeStart.plusMinutes(scheduleld.getLunchLengthInMinutes().toMinutes());
				LocalTime breakTimeStart = scheduleld.getBreakTime();
				LocalTime breakTimeEnd = breakTimeStart.plusMinutes(scheduleld.getBreakLengthInMinutes().toMinutes());

				for(int day=1;day<=6;day++) {
					for(int hour=0;hour<classHoursPerDay;hour++) {
						ClassHour classHour = new ClassHour();
						//Assign a value to roomNo (assuming it's madatory)
						classHour.setRoomNo(100);

						if (currentTime.toLocalTime().equals(breakTimeStart) || 
								(currentTime.toLocalTime().isAfter(breakTimeStart) && currentTime.toLocalTime().isBefore(breakTimeEnd))) {

							classHour.setBeginsAt(currentTime);
							classHour.setEndsAt(currentTime.plusMinutes(scheduleld.getBreakLengthInMinutes().toMinutes()));
							classHour.setClassstatus(classStatus.BREAK_TIME);
							currentTime = classHour.getEndsAt();

						} else if (currentTime.toLocalTime().equals(lunchTimeStart) || 
								(currentTime.toLocalTime().isAfter(lunchTimeStart) && currentTime.toLocalTime().isBefore(lunchTimeEnd))) {

							classHour.setBeginsAt(currentTime);
							classHour.setEndsAt(currentTime.plusMinutes(scheduleld.getLunchLengthInMinutes().toMinutes()));
							classHour.setClassstatus(classStatus.LUNCH_TIME);
							currentTime = classHour.getEndsAt();

						} else {

							LocalDateTime beginsAt = currentTime;
							LocalDateTime endsAt = beginsAt.plusMinutes(classHourLength);

							classHour.setBeginsAt(beginsAt);
							classHour.setEndsAt(endsAt);
							classHour.setClassstatus(classStatus.NOT_SCHEDULED);

							currentTime = endsAt;
						}

						classHour.setAcademicProgram(academicProgram);
						classHourRepository.save(classHour);
					}
					currentTime = currentTime.plusDays(1).with(scheduleld.getOpensAt());
				}
				return ResponseEntity.status(HttpStatus.CREATED)
						.body("Class Hour generated for the current week successfully");
			}else {
				throw new IllegalArgumentException("School doesn't have schedule");
			}
		}).orElseThrow(() -> new IllegalArgumentException("Invalid Program"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(List<ClassHourDTOs> classHourDtoList) {
		List<ClassHourResponse> updatedClassHourResponses = new ArrayList<ClassHourResponse>();

		classHourDtoList.forEach(classHourDTO -> {
			ClassHour existingClassHour = classHourRepository.findById(classHourDTO.getClassHourId())
					.orElseThrow(() -> new IllegalRequestException("ClassHour not found with ID: " + classHourDTO.getClassHourId()));

			Subject subject = subjectrepository.findById(classHourDTO.getSubjectId())
					.orElseThrow(() -> new IllegalRequestException("Subject not found with ID: "+ classHourDTO.getSubjectId() ));

			User teacher = userrepository.findById(classHourDTO.getTeacherId())
					.orElseThrow(() -> new IllegalRequestException("User not found with ID: "  + classHourDTO.getTeacherId()));



			if(existingClassHour != null && subject != null && teacher != null && teacher.getUserRole().equals(UserRole.TEACHER)) {

				if((teacher.getSubject()).equals(subject))
					existingClassHour.setSubject(subject);
				else
					throw new IllegalRequestException("The Teacher is Not Teaching That Subject");
				existingClassHour.setUser(teacher);
				existingClassHour.setRoomNo(classHourDTO.getRoomNo());
				LocalDateTime currentTime = LocalDateTime.now();

				if (existingClassHour.getBeginsAt().isBefore(currentTime) && existingClassHour.getEndsAt().isAfter(currentTime)) {
					existingClassHour.setClassstatus(classStatus.ONGOING);
				} else if (existingClassHour.getEndsAt().isBefore(currentTime)) {
					existingClassHour.setClassstatus(classStatus.COMPLETED);
				} else {
					existingClassHour.setClassstatus(classStatus.UPCOMING);
				}

				existingClassHour=classHourRepository.save(existingClassHour);

				ClassHourResponse classHourResponse = new ClassHourResponse();
				classHourResponse.setBeginsAt(existingClassHour.getBeginsAt());
				classHourResponse.setEndsAt(existingClassHour.getEndsAt());
				classHourResponse.setClassstatus(existingClassHour.getClassstatus());
				classHourResponse.setRoomNo(existingClassHour.getRoomNo());
				updatedClassHourResponses.add(classHourResponse);

			} 
			else {
				throw new IllegalRequestException("Invalid Teacher Id.");
			}
		});
		ResponseStructure<List<ClassHourResponse>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("ClassHours updated successfully!!!!");
		responseStructure.setData(updatedClassHourResponses);


		return new ResponseEntity<ResponseStructure<List<ClassHourResponse>>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> AutoRepeatNextWeekClassHours() {
	    // Set the current date to be within next week
	    LocalDateTime currentDateWithinNextWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).plusWeeks(1);

	    // Step 1: Calculate next week's start date (Monday)
	    LocalDateTime nextMonday = currentDateWithinNextWeek.with(DayOfWeek.MONDAY).plusWeeks(1);

	    // Step 2: Check if class hours are already generated for next week
	    List<ClassHour> existingClassHours = classHourRepository.findByBeginsAtBetween(nextMonday, nextMonday.plusDays(6));

	    if (existingClassHours.isEmpty()) {
	        // Step 3: Copy class hours data from the previous week and adjust dates
	        LocalDateTime previousSaturday = nextMonday.minusDays(2);
	        LocalDateTime previousMonday = previousSaturday.minusDays(5);

	        List<ClassHour> previousWeekClassHours = classHourRepository.findByBeginsAtBetween(previousMonday, previousSaturday);

	        int totalGeneratedClassHours =0;

	        // Adjust and save the class hours for the next week
	        for (ClassHour previousWeekClassHour : previousWeekClassHours) {
	            // Check if adding this class hour exceeds the total of 72
	            if (totalGeneratedClassHours < 72) {
	                ClassHour newClassHour = new ClassHour();
	                newClassHour.setBeginsAt(nextMonday.plusDays(previousWeekClassHour.getBeginsAt().getDayOfWeek().getValue() - 1));
	                newClassHour.setEndsAt(newClassHour.getBeginsAt().plus(previousWeekClassHour.getEndsAt().toLocalTime().toSecondOfDay(), ChronoUnit.SECONDS));
	                newClassHour.setAcademicProgram(previousWeekClassHour.getAcademicProgram());
	                newClassHour.setRoomNo(previousWeekClassHour.getRoomNo());
	                newClassHour.setSubject(previousWeekClassHour.getSubject());
	                newClassHour.setUser(previousWeekClassHour.getUser());
	                newClassHour.setClassstatus(previousWeekClassHour.getClassstatus());

	                classHourRepository.save(newClassHour);

	                // Increment the total count
	                totalGeneratedClassHours++;
	            } else {
	                // Break out of the loop if the total count reaches 72
	                break;
	            }
	        }

	        return new ResponseEntity<>("Class hours for next week generated successfully", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Class hours for next week already exist", HttpStatus.OK);
	    }
	}






}