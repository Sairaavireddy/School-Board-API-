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
import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.exception.UserNotFoundExceptionById;
import com.school.SchoolBoardAPI.repository.AcademicProgramRepository;
import com.school.SchoolBoardAPI.repository.SchoolRepository;
import com.school.SchoolBoardAPI.repository.UserRepository;
import com.school.SchoolBoardAPI.requestdto.AcademicProgramRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.service.AcademicProgramService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
@Service
public class AcademicProgramServiceImpl implements AcademicProgramService {
    @Autowired
	private  AcademicProgramRepository academicprogramrepository;
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	@Autowired
	private SchoolRepository schoolrepository;
	@Autowired
	private UserRepository userrepository;
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
	
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> assignUser(
	        int programId, int userId) {
	    AcademicProgram academicProgram = academicprogramrepository.findById(programId)
	            .orElseThrow(() -> new IllegalRequestException("Academic Program not found"));

	    // Validate the user
	    User user = userrepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundExceptionById("User not found"));

	    // Check if the user is an ADMIN
	    if (user.getUserRole() == UserRole.ADMIN) {
	        throw new IllegalRequestException("Admin cannot be associated with any Academic Program");
	    }

	    // Determine the role of the user in the Academic Program
	    UserRole userRoleInProgram = determineUserRoleForAcademicProgram(user);

	    // Assign the user to the Academic Program using lambda expressions and map method
	    academicProgram.getUserlist().addAll(
	            userRoleInProgram == UserRole.TEACHER ?
	                    Collections.singletonList(user) : Collections.emptyList()
	    );

	    academicProgram.getUserlist().addAll(
	            userRoleInProgram == UserRole.STUDENT ?
	                    Collections.singletonList(user) : Collections.emptyList()
	    );

	    academicprogramrepository.save(academicProgram);

	    // Return the response
	    ResponseStructure<AcademicProgramResponse> structure = new ResponseStructure<>();
	    structure.setStatus(HttpStatus.CREATED.value());
	    structure.setMessage("User assigned to Academic Program successfully");
	    structure.setData(mapToAcademicProgramResponse(academicProgram));
	    return ResponseEntity.ok(structure);
	}

	private UserRole determineUserRoleForAcademicProgram(User user) {
	    // You may implement your logic to determine the role of the user in the Academic Program
	    // For example, check user roles, or based on some other business logic.
	    // This is just a placeholder, adapt it based on your actual requirements.
	    return user.getUserRole();
	}


	private AcademicProgram mapToAcademicProgram(AcademicProgramRequest academicprogramrequest) {
	    return AcademicProgram.builder()
	            .ProgramName(academicprogramrequest.getProgramName())
	            .programtype(academicprogramrequest.getProgramtype())
	            .beginsAt(academicprogramrequest.getBeginsAt())
	            .endsAt(academicprogramrequest.getEndsAt())
	            .build();
	}
	public AcademicProgramResponse mapToAcademicProgramResponse(AcademicProgram academicProgram) {
	    return AcademicProgramResponse.builder()
	            .programId(academicProgram.getProgramId())
	            .programtype(academicProgram.getProgramtype())
	            .ProgramName(academicProgram.getProgramName())
	            .beginsAt(academicProgram.getBeginsAt())
	            .endsAt(academicProgram.getEndsAt())
	            .Slist(academicProgram.getSlist())
	            .build();
	    
	}
	
	

}
