package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.requestdto.SchoolRequest;
import com.school.SchoolBoardAPI.responsedto.SchoolResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface SchoolService {
	public ResponseEntity<ResponseStructure<SchoolResponse>> SaveSchool(SchoolRequest schoolrequest);
//    public ResponseEntity<ResponseStructure<School>> findSchool(int schoolId);
//    public ResponseEntity<ResponseStructure<School>> updatedSchool(int schoolId, School updatedschool);
    public ResponseEntity<ResponseStructure<SchoolResponse>>  deleteSchool(int schoolId);
//	
	
}
