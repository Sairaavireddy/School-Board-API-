package com.school.SchoolBoardAPI.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.repository.AcademicProgramRepository;
import com.school.SchoolBoardAPI.repository.SchoolRepository;
import com.school.SchoolBoardAPI.service.AcademicProgramService;
import com.school.SchoolBoardAPI.userrequestdto.AcademicProgramRequest;
import com.school.SchoolBoardAPI.userresponsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
@Service
public class AcademicProgramImpl implements AcademicProgramService {
    @Autowired
	private  AcademicProgramRepository academicprogramrepository;
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	@Autowired
	private SchoolRepository schoolrepository;
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveacademicprogram(int schoolId,
	        AcademicProgramRequest academicprogramrequest) {
	    return schoolrepository.findById(schoolId).map(s -> {
	        AcademicProgram academicProgram = mapToAcademicProgram(academicprogramrequest);
	        academicProgram.setSchool(s); // Set the school for the program
	        academicProgram = academicprogramrepository.save(academicProgram);
	        s.getAplist().add(academicProgram); // Add to the existing list
	        schoolrepository.save(s);

	        structure.setStatus(HttpStatus.CREATED.value());
	        structure.setMessage("Academic Program object created Successfully");
	        structure.setData(mapToAcademicProgramResponse(academicProgram));
	        return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.CREATED);
	    }).orElseThrow(() -> new IllegalRequestException("School not found"));
	}
	@Override
	public List<AcademicProgramResponse> findallAcademicPrograms(int schoolId) {
	    Optional<School> optionalSchool = schoolrepository.findById(schoolId);
	    if (optionalSchool.isPresent()) {
	        School school = optionalSchool.get();
	        List<AcademicProgram> academicPrograms = school.getAplist();
	        List<AcademicProgramResponse> responses = new ArrayList<>();
	        for (AcademicProgram academicProgram : academicPrograms) {
	            responses.add(mapToAcademicProgramResponse(academicProgram));
	        }
	        return responses;
	    } else {
	        return Collections.emptyList();
	    }
	}
	private AcademicProgram mapToAcademicProgram(AcademicProgramRequest academicprogramrequest) {
	    return AcademicProgram.builder()
	            .ProgramName(academicprogramrequest.getProgramName())
	            .programtype(academicprogramrequest.getProgramtype())
	            .beginsAt(academicprogramrequest.getBeginsAt())
	            .endsAt(academicprogramrequest.getEndsAt())
	            .build();
	}
	private AcademicProgramResponse mapToAcademicProgramResponse(AcademicProgram academicProgram) {
	    return AcademicProgramResponse.builder()
	            .programId(academicProgram.getProgramId())
	            .programtype(academicProgram.getProgramtype())
	            .ProgramName(academicProgram.getProgramName())
	            .beginsAt(academicProgram.getBeginsAt())
	            .endsAt(academicProgram.getEndsAt())
	            .build();
	    
	}

	

}
