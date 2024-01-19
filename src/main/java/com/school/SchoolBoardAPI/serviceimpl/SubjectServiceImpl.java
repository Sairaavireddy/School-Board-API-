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
}
