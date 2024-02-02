package com.school.SchoolBoardAPI.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.exception.UserNotFoundExceptionById;
import com.school.SchoolBoardAPI.repository.AcademicProgramRepository;
import com.school.SchoolBoardAPI.repository.ClassHourRepository;
import com.school.SchoolBoardAPI.repository.SchoolRepository;
import com.school.SchoolBoardAPI.repository.UserRepository;
import com.school.SchoolBoardAPI.requestdto.SchoolRequest;
import com.school.SchoolBoardAPI.responsedto.SchoolResponse;
import com.school.SchoolBoardAPI.service.SchoolService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
import com.school.SchoolBoardAPI.utility.ScheduledJobs;

import jakarta.transaction.Transactional;
@Service
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	private SchoolRepository schoolrepository;
	@Autowired
	private ResponseStructure<SchoolResponse> structure;
	@Autowired
	UserRepository userrepository;
	@Autowired
	UserServiceImpl userserviceImpl;
	@Autowired
	AcademicProgramServiceImpl academicprogramserviceimpl;
	@Autowired
	ClassHourRepository classhourRepository;

	@Autowired
	private AcademicProgramRepository academicprogramrepository;
	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> SaveSchool(SchoolRequest schoolrequest) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userrepository.findByusername(username).map(u->{
			if(u.getUserRole().equals(UserRole.ADMIN)) {
				if(u.getSchool()==null) {
					School school = mapToSchool(schoolrequest);
					school = schoolrepository.save(school);
					u.setSchool(school);
					userrepository.save(u);
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("school object created Sucessfully");
					structure.setData(mapToSchoolResponse(school, false));
					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);

				}else
					throw new IllegalRequestException("school object is already present");

			}else
				throw new IllegalRequestException("Only admin can create the school");
		}).orElseThrow(()->new  UserNotFoundExceptionById());



	}

	//	@Override
	//	public Optional<School> findSchool(int schoolId) {
	//		return schoolrepository.findById(schoolId);
	//
	//	}
	//
	//
	//	@Override
	//	public School updatedSchool(int schoolId, School updatedschool) {
	//		Optional<School> optional = schoolrepository.findById(schoolId);
	//		if(optional.isPresent())
	//		{
	//			School existingschool= optional.get();
	//			updatedschool.setSchoolId(existingschool.getSchoolId());
	//			School school = schoolrepository.save(updatedschool);
	//			return school;  
	//		}
	//		else {
	//			return null;
	//		}

	//	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId) {
		return schoolrepository.findById(schoolId)
				.map(school -> {
					school.setDeleted(true);

					schoolrepository.save(school);
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("school deleted successfully");
					structure.setData(mapToSchoolResponse(school,true));


					return new ResponseEntity<>(structure, HttpStatus.OK);
				})
				.orElseThrow(() -> new UserNotFoundExceptionById("school not found by id"));

	}


	private School mapToSchool(SchoolRequest schoolrequest) {
		return School.builder()
				.schoolName(schoolrequest.getSchoolName())
				.contactNo(schoolrequest.getContactNo())
				.emailId(schoolrequest.getEmailId())
				.address(schoolrequest.getAddress())
				.build();
	}
	private SchoolResponse mapToSchoolResponse(School school, boolean b) {
		return SchoolResponse.builder()
				.schoolName(school.getSchoolName())
				.contactNo(school.getContactNo())
				.emailId(school.getEmailId())
				.address(school.getAddress())
				.isDeleted(b)
				.build();
	}
	@Transactional
	public void deleteschools() {
	    List<School> schools = schoolrepository.findByIsDeleted(true);
	    schools.forEach(school -> {
	        List<AcademicProgram> academicPrograms = school.getAplist();

	        // Delete associated classHour 
	        for (AcademicProgram academicProgram : academicPrograms) {
	            classhourRepository.deleteByAcademicProgramProgramId(academicProgram.getProgramId());
	        }

	        // delete the academic_programs
	        academicprogramrepository.deleteAll(academicPrograms);

	        List<User> users = userrepository.findBySchool(school);
	        users.forEach(user -> {
	            if (user.getUserRole().equals(UserRole.ADMIN)) {
	                user.setSchool(null);
	                userrepository.save(user);
	            } else {
	                userrepository.delete(user);
	            }
	        });

	        schoolrepository.delete(school);
	        System.err.println("School is Deleted Successfully !!");
	    });
	}

	
}
	
