package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface SchoolService {
	public ResponseEntity<ResponseStructure<School>> SaveSchool(School school);
//    public ResponseEntity<ResponseStructure<School>> findSchool(int schoolId);
//    public ResponseEntity<ResponseStructure<School>> updatedSchool(int schoolId, School updatedschool);
//    public ResponseEntity<ResponseStructure<School>> deleteSchool();
//	
	
}
