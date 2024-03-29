package com.school.SchoolBoardAPI.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.entity.Subject;
import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.exception.UserNotFoundExceptionById;
import com.school.SchoolBoardAPI.repository.AcademicProgramRepository;
import com.school.SchoolBoardAPI.repository.ClassHourRepository;
import com.school.SchoolBoardAPI.repository.SchoolRepository;
import com.school.SchoolBoardAPI.repository.UserRepository;
import com.school.SchoolBoardAPI.requestdto.AcademicProgramRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.responsedto.UserResponse;
import com.school.SchoolBoardAPI.service.AcademicProgramService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

import jakarta.transaction.Transactional;
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
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ClassHourRepository classhourrepository;
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveacademicprogram(int schoolId,
			AcademicProgramRequest academicprogramrequest) {
		return schoolrepository.findById(schoolId).map(s -> {
			AcademicProgram academicProgram = mapToAcademicProgram(academicprogramrequest);
			academicProgram.setSchool(s);// Set the school for the program

			
			academicProgram = academicprogramrepository.save(academicProgram);
			s.getAplist().add(academicProgram); // Add to the existing list
			schoolrepository.save(s);

			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Academic Program object created Successfully");
			structure.setData(mapToAcademicProgramResponse(academicProgram, false));
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
				responses.add(mapToAcademicProgramResponse(academicProgram, false));
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
		List<Subject> subjects = academicProgram.getSlist();
		// Validate the user
		User user = userrepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundExceptionById("User not found"));

		// Check if the user is an ADMIN
		if (user.getUserRole() == UserRole.ADMIN) {
			throw new IllegalRequestException("Admin cannot be associated with any Academic Program");
		}
		if (user.getUserRole() != UserRole.TEACHER && user.getUserRole() != UserRole.STUDENT) {
			throw new IllegalRequestException("User must have role TEACHER or STUDENT.");
		}
		else if(user.getUserRole().equals(UserRole.TEACHER)&&!subjects.contains(user.getSubject())){
			throw new IllegalRequestException("Teacher Subject doesnot matches");
		}
		// Add the user to the academic program
		if (!academicProgram.getUserlist().contains(user)) {
			academicProgram.getUserlist().add(user);
			academicprogramrepository.save(academicProgram);

			// Return the response
			ResponseStructure<AcademicProgramResponse> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("User assigned to Academic Program successfully");
			structure.setData(mapToAcademicProgramResponse(academicProgram, false));
			return ResponseEntity.ok(structure);
		} else {
			throw new IllegalRequestException("User is already associated with the academic program");
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
	public AcademicProgramResponse mapToAcademicProgramResponse(AcademicProgram academicProgram, boolean b) {
		return AcademicProgramResponse.builder()
				.programId(academicProgram.getProgramId())
				.programtype(academicProgram.getProgramtype())
				.ProgramName(academicProgram.getProgramName())
				.beginsAt(academicProgram.getBeginsAt())
				.endsAt(academicProgram.getEndsAt())
				.Slist(academicProgram.getSlist())
				.isDeleted(b)
				.build();

	}


	@Override
	public ResponseEntity<ResponseStructure<List<User>>> findUsersInProgram(int programId, String userRole) {
		List<User> users = academicprogramrepository.findById(programId)
		        .map(program -> userrepository.findByUserRoleAndListAcademicPrograms(UserRole.valueOf(userRole.toUpperCase()), program))
		        .orElseThrow();

		ResponseStructure<List<User>> responseStructure = new ResponseStructure<List<User>>();
		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("Fetched successfully!!!");
		responseStructure.setData (users);

		return new ResponseEntity<ResponseStructure<List<User>>> (responseStructure, HttpStatus.OK);

	}


	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteprogram(int programId) {
		return academicprogramrepository.findById(programId)
				.map(program-> {
					program.setDeleted(true);

					academicprogramrepository.save(program);
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("academicprogram deleted successfully");
					structure.setData(mapToAcademicProgramResponse(program,true));


					return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.OK);
				})
				.orElseThrow(() -> new IllegalRequestException("program Id not found"));
	}


	public void deleteprograms() {
		List<AcademicProgram> programs =academicprogramrepository.findByIsDeleted(true);
		programs.forEach(program -> {
		    classhourrepository.deleteAll(program.getClassHourlist());
		    academicprogramrepository.deleteAll(programs);
		});

	}
	


}








