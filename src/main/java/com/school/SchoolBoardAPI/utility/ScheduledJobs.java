package com.school.SchoolBoardAPI.utility;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.SchoolBoardAPI.entity.ClassHour;
import com.school.SchoolBoardAPI.enums.classStatus;
import com.school.SchoolBoardAPI.repository.ClassHourRepository;
import com.school.SchoolBoardAPI.serviceimpl.AcademicProgramServiceImpl;
import com.school.SchoolBoardAPI.serviceimpl.SchoolServiceImpl;
import com.school.SchoolBoardAPI.serviceimpl.UserServiceImpl;

@Component
public class ScheduledJobs {
	@Autowired
	private UserServiceImpl userserviceimpl;
	@Autowired
	private AcademicProgramServiceImpl academicProgramimpl;
	@Autowired
	private SchoolServiceImpl schoolserviceimpl;
	@Autowired
	private ClassHourRepository classHourRepository;
//	@Transactional
//	@Scheduled(fixedDelay =1000l*60)
//	public void deleteuser() {
//		userserviceimpl.deleteUsers();
//
//	}
//	@Transactional
//	@Scheduled(fixedDelay =1000l*60)
//	public void deleteprogram() {
//		academicProgramimpl.deleteprograms();
//
//	}
//	@Transactional
//	@Scheduled(fixedDelay =1000l*60)
//	public void deleteschool() {
//		schoolserviceimpl.deleteschools();
//	} 
	@Scheduled(fixedDelay =5000l*60)
	 public void updateClassHourStatus() {
	        LocalDateTime currentTime = LocalDateTime.now();

	        // Retrieve the class hours for the current day and time
	        List<ClassHour> classHours = classHourRepository.findByBeginsAtBeforeAndEndsAtAfter(currentTime, currentTime);

	        for (ClassHour classHour : classHours) {
	            if (currentTime.equals(classHour.getBeginsAt())) {
	                // Set status as ongoing if the current time is the class hour begin time
	                classHour.setClassstatus(classStatus.ONGOING);
	            } else if (currentTime.equals(classHour.getEndsAt())) {
	                // Set status as completed if the current time is the class hour end time
	                classHour.setClassstatus(classStatus.COMPLETED);
	            } else {
	                // Handle any other logic if needed
	            }

	            // Update the class hour in the repository
	            classHourRepository.save(classHour);
	        }
	    }
	 
}




