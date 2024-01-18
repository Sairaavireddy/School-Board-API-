package com.school.SchoolBoardAPI.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.Subject;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.repository.AcademicProgramRepository;
import com.school.SchoolBoardAPI.repository.SujectRepository;
import com.school.SchoolBoardAPI.requestdto.SubjectRequest;
import com.school.SchoolBoardAPI.responsedto.AcademicProgramResponse;
import com.school.SchoolBoardAPI.service.SubjectService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SujectRepository subjectrepository;
	@Autowired
	private AcademicProgramRepository academicProgramRepository;
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	@Autowired
	private AcademicProgramServiceImpl academicprogramserviceimpl;
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(int programId, SubjectRequest subjectRequest) {
		return academicProgramRepository.findById(programId)
				.map(program -> {
					List<Subject> subjects = new ArrayList<>();

					subjectRequest.getSubjectName().forEach(subjectName -> {
						String lowercaseName = subjectName.toLowerCase();

						Subject subject = subjectrepository.findBySubjectName(lowercaseName)
								.map(existingSubject -> existingSubject)
								.orElseGet(() -> {
									Subject newSubject = new Subject();
									newSubject.setSubjectName(lowercaseName);
									return subjectrepository.save(newSubject);
								});

						subjects.add(subject);
					});

					program.setSlist(subjects);
					academicProgramRepository.save(program);

					ResponseStructure<AcademicProgramResponse> structure = new ResponseStructure<>();
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("Updated the subject list to Academic Program");
					structure.setData(academicprogramserviceimpl.mapToAcademicProgramResponse(program));
					return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.CREATED);
				})
				.orElseThrow(() -> new IllegalRequestException("Academic Program not found"));
	}


}
