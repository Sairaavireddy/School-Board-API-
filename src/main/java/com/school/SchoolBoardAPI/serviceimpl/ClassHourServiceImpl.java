package com.school.SchoolBoardAPI.serviceimpl;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
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
import com.school.SchoolBoardAPI.requestdto.ExcelRequestDto;
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
		AcademicProgram program = academicProgramRepository.findById(programId).orElse(null);

		if (program == null)
			throw new IllegalArgumentException("Program Does Not Exist!!!");

		LocalDate recordStartDate = (program.getBeginsAt().isAfter(LocalDate.now()))? program.getBeginsAt() : LocalDate.now();
        
		List<ClassHour> generatedClassHours = generateClassHoursForWeek(program, recordStartDate);
		List<ClassHour> savedClassHours = classHourRepository.saveAll(generatedClassHours);

		List<ClassHourResponse> classHourResponses = savedClassHours.stream().map(this::mapToResponse)
				.collect(Collectors.toList());

		ResponseStructure<List<ClassHourResponse>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("ClassHours created successfully!!!!");
		responseStructure.setData(classHourResponses);

		return new ResponseEntity<String>(HttpStatus.CREATED);
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



	private List<ClassHour> generateClassHoursForWeek(AcademicProgram program, LocalDate startingDate) {
	    List<ClassHour> classHours = new ArrayList<>();
	    Schedule schedule = program.getSchool().getSchedule();
	    Duration classDuration = schedule.getClassHoursLengthInMinutes();
	    Duration lunchDuration = schedule.getLunchLengthInMinutes();
	    Duration breakDuration = schedule.getBreakLengthInMinutes();
	    LocalTime breakTime = schedule.getBreakTime();
	    LocalTime lunchTime = schedule.getLunchTime();
	    Duration topUp = Duration.ofMinutes(2);

	    // Find the next Monday from the starting date
	    LocalDate nextMonday = startingDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

	    for (int dayOfWeek = 0; dayOfWeek < 6; dayOfWeek++) { // Iterate from Monday to Saturday

	        LocalDate currentDate = nextMonday.plusDays(dayOfWeek);
	        LocalTime opensAt = schedule.getOpensAt();
	        LocalTime endsAt = opensAt.plus(classDuration);
	        classStatus status = classStatus.NOT_SCHEDULED;

	        for (int classPerDay = schedule.getClassHoursPerDay(); classPerDay > 0; classPerDay--) {
	            ClassHour classHour = ClassHour.builder()
	                    .beginsAt(LocalDateTime.of(currentDate, opensAt))
	                    .endsAt(LocalDateTime.of(currentDate, endsAt))
	                    .roomNo(0)
	                    .classstatus(status)
	                    .academicProgram(program)
	                    .build();

	            classHours.add(classHour);

	            if (breakTime.isAfter(opensAt.minus(topUp)) && breakTime.isBefore(endsAt.plus(topUp))) {
	                opensAt = opensAt.plus(breakDuration);
	                endsAt = endsAt.plus(breakDuration);
	                ClassHour classHour2 = ClassHour.builder()
		                    .beginsAt(LocalDateTime.of(currentDate, opensAt.minus(breakDuration)))
		                    .endsAt(LocalDateTime.of(currentDate, opensAt))
		                    .roomNo(0)
		                    .classstatus(classStatus.BREAK_TIME)
		                    .academicProgram(program)
		                    .build();

		            classHours.add(classHour2);
	                
	            } else if (lunchTime.isAfter(opensAt.minus(topUp)) && lunchTime.isBefore(endsAt.plus(topUp))) {
	                opensAt = opensAt.plus(lunchDuration);
	                endsAt = endsAt.plus(lunchDuration);
	                ClassHour classHour2 = ClassHour.builder()
		                    .beginsAt(LocalDateTime.of(currentDate, opensAt.minus(lunchDuration )))
		                    .endsAt(LocalDateTime.of(currentDate, opensAt))
		                    .roomNo(0)
		                    .classstatus(classStatus.LUNCH_TIME)
		                    .academicProgram(program)
		                    .build();

		            classHours.add(classHour2);
	            }

	            opensAt = endsAt;
	            endsAt = opensAt.plus(classDuration);
	        }
	    }

	    return classHours;
	}
	
	private ClassHourResponse mapToResponse(ClassHour classHour) {
		return ClassHourResponse.builder().beginsAt(classHour.getBeginsAt())
				.endsAt(classHour.getEndsAt()).roomNo(classHour.getRoomNo()).classstatus(classHour.getClassstatus())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> AutoRepeatNextWeekClassHours() {
		ClassHour lastRecord = classHourRepository.findTopByOrderByClassHourIdDesc().get();
		 LocalDate endDate =lastRecord.getBeginsAt().toLocalDate();
			AcademicProgram academicProgram=lastRecord.getAcademicProgram();
			List<ClassHour> previousWeekClassHours = classHourRepository.findByAcademicProgramAndBeginsAtBetween(academicProgram, endDate.minusDays(5).atStartOfDay(), endDate.atStartOfDay().plusDays(1));
        if (previousWeekClassHours.isEmpty()) {
             throw new IllegalArgumentException("The classHour Is Empty");
        }
        List<ClassHour> savedClassHours = new ArrayList<>();
        List<ClassHour> nextWeekClassHours = generateClassHoursForWeek(previousWeekClassHours.get(0).getAcademicProgram(), endDate);
        for (ClassHour nextWeek : nextWeekClassHours) {
            for (ClassHour previous : previousWeekClassHours) {
               if(nextWeek.getBeginsAt().getDayOfWeek()==previous.getBeginsAt().getDayOfWeek()&&nextWeek.getBeginsAt().toLocalTime().equals(previous.getBeginsAt().toLocalTime())) {
            	   if(previous.getClassstatus()!=classStatus.NOT_SCHEDULED) {
            		   nextWeek.setRoomNo(previous.getRoomNo());
            		   nextWeek.setClassstatus(classStatus.UPCOMING);
            		   nextWeek.setUser(previous.getUser());
            		   nextWeek.setSubject(previous.getSubject());
            		   
            	   }
            	   ClassHour classHour=classHourRepository.save(nextWeek);
        		   savedClassHours.add(classHour);
               }
            }
        }
      
        
        List<ClassHourResponse> classHourResponses = savedClassHours.stream().map(this::mapToResponse).collect(Collectors.toList());

        
        ResponseStructure<List<ClassHourResponse>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("ClassHours updated successfully!!!!");
		responseStructure.setData(classHourResponses);


		return new ResponseEntity<ResponseStructure<List<ClassHourResponse>>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> excelSheet(int programId, ExcelRequestDto excelRequestDto) throws Exception, IOException {
		LocalDateTime startDateTime=excelRequestDto.getFromDate().atStartOfDay();
		LocalDateTime endDateTime=excelRequestDto.getToDate().atStartOfDay().plusDays(1);
		AcademicProgram program=academicProgramRepository.findById(programId).get();
		List<ClassHour> listClassHours = classHourRepository.findByAcademicProgramAndBeginsAtBetween(program, startDateTime, endDateTime);
		XSSFWorkbook workbook=new XSSFWorkbook();
		Sheet sheet=workbook.createSheet();

		int rowNumber=0;
		Row header=sheet.createRow(rowNumber);
		header.createCell(0).setCellValue("Begin Date");
		header.createCell(1).setCellValue("Begin Time");
		header.createCell(2).setCellValue("End Date");
		header.createCell(3).setCellValue("End Time");
		header.createCell(4).setCellValue("Subject");
		header.createCell(5).setCellValue("Teacher");
		header.createCell(6).setCellValue("Room No");

		DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm"); 
		DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		for(ClassHour classHour : listClassHours) {
			Row row = sheet.createRow(++rowNumber);
			row.createCell(0).setCellValue(date.format(classHour.getBeginsAt()));
			row.createCell(1).setCellValue(time.format(classHour.getBeginsAt()));
			row.createCell(2).setCellValue(date.format(classHour.getEndsAt()));
			row.createCell(3).setCellValue(time.format(classHour.getEndsAt()));
			if(classHour.getSubject()==null&&classHour.getUser()==null) {
				row.createCell(4).setCellValue("");
				row.createCell(5).setCellValue("");	
			}else {
				row.createCell(4).setCellValue(classHour.getSubject().getSubjectName());
				row.createCell(5).setCellValue(classHour.getUser().getUsername());	
			}
			row.createCell(6).setCellValue(classHour.getRoomNo());
		}
		try {
			FileOutputStream fileOut;
			fileOut = new FileOutputStream(excelRequestDto.getFilePath());
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed To Store in Exel Sheet");
		}
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("Sucefully Saved in Exel Sheet !!!!");
		responseStructure.setData("Sucefully Saved in Exel Sheet from Date :"+excelRequestDto.getFromDate()+" To Date:"+excelRequestDto.getToDate());

		return ResponseEntity.ok("Excel sheet generated sucessfully");
	}


}	