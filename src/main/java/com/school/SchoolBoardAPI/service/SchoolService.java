package com.school.SchoolBoardAPI.service;

import org.springframework.http.ResponseEntity;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.userrequestdto.SchoolRequest;
import com.school.SchoolBoardAPI.userresponsedto.SchoolResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

public interface SchoolService {
	public ResponseEntity<ResponseStructure<SchoolResponse>> SaveSchool(int userId,SchoolRequest schoolrequest);
//    public ResponseEntity<ResponseStructure<School>> findSchool(int schoolId);
//    public ResponseEntity<ResponseStructure<School>> updatedSchool(int schoolId, School updatedschool);
    public void  deleteSchool(int schoolId);
//	
	
}
