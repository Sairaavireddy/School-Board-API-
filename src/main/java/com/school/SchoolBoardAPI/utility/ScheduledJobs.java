package com.school.SchoolBoardAPI.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.SchoolBoardAPI.serviceimpl.AcademicProgramServiceImpl;
import com.school.SchoolBoardAPI.serviceimpl.SchoolServiceImpl;
import com.school.SchoolBoardAPI.serviceimpl.UserServiceImpl;

import jakarta.transaction.Transactional;

@Component
public class ScheduledJobs {
	@Autowired
	private UserServiceImpl userserviceimpl;
	@Autowired
	private AcademicProgramServiceImpl academicProgramimpl;
	@Autowired
	private SchoolServiceImpl schoolserviceimpl;
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


}

