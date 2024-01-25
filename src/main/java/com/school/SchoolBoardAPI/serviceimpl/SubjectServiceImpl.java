package com.school.SchoolBoardAPI.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.Subject;
import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.repository.AcademicProgramRepository;
import com.school.SchoolBoardAPI.repository.SubjectRepository;
import com.school.SchoolBoardAPI.repository.UserRepository;
import com.school.SchoolBoardAPI.requestdto.SubjectRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.responsedto.SubjectResponse;
import com.school.SchoolBoardAPI.responsedto.UserResponse;
import com.school.SchoolBoardAPI.service.SubjectService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepository subjectrepository;
	@Autowired
	private AcademicProgramRepository academicProgramRepository;
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	@Autowired
	private AcademicProgramServiceImpl academicprogramserviceimpl;
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private UserServiceImpl userserviceimpl;
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(int programId, SubjectRequest subjectRequest) {
		return academicProgramRepository.findById(programId)
				.map(program -> {
					List<Subject> subjects = (program.getSlist() != null) ? program.getSlist() : new ArrayList<>();

					// Add new subjects specified by the client
					subjectRequest.getSubjectName().forEach(name -> {
						boolean isPresent = subjects.stream().anyMatch(subject -> name.equalsIgnoreCase(subject.getSubjectName()));
						if (!isPresent) {
							subjects.add(subjectrepository.findBySubjectName(name)
									.orElseGet(() -> subjectrepository.save(Subject.builder().subjectName(name).build())));
						}
					});

					// Remove subjects that are not specified by the client
					List<Subject> toBeRemoved = new ArrayList<>();
					subjects.forEach(subject -> {
						boolean isPresent = subjectRequest.getSubjectName().stream()
								.anyMatch(name -> subject.getSubjectName().equalsIgnoreCase(name));
						if (!isPresent) {
							toBeRemoved.add(subject);
						}
					});
					subjects.removeAll(toBeRemoved);

					program.setSlist(subjects);
					academicProgramRepository.save(program);

					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("Created the subject list for the Academic Program");
					structure.setData(academicprogramserviceimpl.mapToAcademicProgramResponse(program));
					return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.CREATED);
				})
				.orElseThrow(() -> new IllegalRequestException("Academic Program not found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects() {
		List<Subject> subjects = subjectrepository.findAll();

		// Return the response
		ResponseStructure<List<SubjectResponse>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("Subjects fetched successfully");
		responseStructure.setData(mapToSubjectResponses(subjects));
		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	private List<SubjectResponse> mapToSubjectResponses(List<Subject> subjects) {
		return subjects.stream()
				.map(subject -> new SubjectResponse(subject.getSubjectId(), subject.getSubjectName()))
				.collect(Collectors.toList());
	}

	//	@Override
	//	public ResponseEntity<ResponseStructure<UserResponse>> assignSubjectToUser(int subjectId, int userId) {
	//		 // Validate if the subject exists
	//        Subject subject = subjectrepository.findById(subjectId)
	//                .orElseThrow(() -> new IllegalRequestException("Subject not found with ID"));
	//
	//        // Validate if the user exists
	//        User user = userrepository.findById(userId)
	//                .orElseThrow(() -> new IllegalRequestException("User not found with ID"));
	//
	//        // Check if the user has the role "ADMIN" or "STUDENT"
	//        if (user.getUserRole() == UserRole.ADMIN || user.getUserRole() == UserRole.STUDENT) {
	//            throw new IllegalRequestException("Cannot assign subject to users with role ADMIN or STUDENT");
	//        }
	//
	//        // Assign the subject to the user
	//        if (!((List<Subject>) user.getSubject()).contains(subject)) {
	//            ((List<Subject>) user.getSubject()).add(subject);
	//            userrepository.save(user);
	//            structure.setStatus(HttpStatus.OK.value());
	//    	    structure.setMessage("Subject is  assigned to the user");
	//    	    structure.setData(Collections.singletonList(mapToSubjectResponses(subject)));
	//
	//    	    return new ResponseEntity<ResponseStructure<UserResponse>>(HttpStatus.OK);
	//        } else {
	//              throw new IllegalRequestException("Subject is already assigned to the user");
	//        }
	//    }

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> assignSubjectToUser(int subjectId, int userId) {
		Subject subject = subjectrepository.findById(subjectId)
				.orElseThrow(() -> new IllegalRequestException("Subject not found with ID"));

		// Validate if the user exists
		User user = userrepository.findById(userId)
				.orElseThrow(() -> new IllegalRequestException("User not found with ID"));

		// Check if the user has the role "ADMIN" or "STUDENT"
		if (user.getUserRole() == UserRole.ADMIN || user.getUserRole() == UserRole.STUDENT) {
			throw new IllegalRequestException("Cannot assign subject to ADMIN or STUDENT");
		}

		user.setSubject(subject);

		userrepository.save(user);
		ResponseStructure<UserResponse> structure = new ResponseStructure<>();
		structure.setStatus(HttpStatus.OK.value());
		structure.setMessage("Subject is assigned to the user");
		structure.setData(userserviceimpl.mapToUserResponse(user,false));
		return new ResponseEntity<>(structure, HttpStatus.OK);
	} 



	SubjectResponse mapToSubjectResponses(Subject subject) {
		return SubjectResponse.builder()
				.subjectId(subject.getSubjectId())
				.subjectName(subject.getSubjectName())
				.build();
	}

}

