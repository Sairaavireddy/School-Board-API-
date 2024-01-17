package com.school.SchoolBoardAPI.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.enums.UserRole;
import com.school.SchoolBoardAPI.exception.IllegalRequestException;
import com.school.SchoolBoardAPI.exception.UserNotFoundExceptionById;
import com.school.SchoolBoardAPI.repository.SchoolRepository;
import com.school.SchoolBoardAPI.repository.UserRepository;
import com.school.SchoolBoardAPI.service.SchoolService;
import com.school.SchoolBoardAPI.userrequestdto.SchoolRequest;
import com.school.SchoolBoardAPI.userresponsedto.SchoolResponse;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
@Service
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	private SchoolRepository schoolrepository;
	@Autowired
	private ResponseStructure<SchoolResponse> structure;
    @Autowired
    UserRepository userrepository;
	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> SaveSchool(int userId,SchoolRequest schoolrequest) {
		return userrepository.findById(userId).map(u->{
			if(u.getUserRole().equals(UserRole.ADMIN)) {
				if(u.getSchool()==null) {
					School school = mapToSchool(schoolrequest);
					school = schoolrepository.save(school);
					u.setSchool(school);
					userrepository.save(u);
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("school object created Sucessfully");
					structure.setData(mapToSchoolResponse(school));
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
	public void deleteSchool(int schoolId) {
		// TODO Auto-generated method stub
		schoolrepository.deleteById(schoolId);
		
	}


	 private School mapToSchool(SchoolRequest schoolrequest) {
	        return School.builder()
	                .schoolName(schoolrequest.getSchoolName())
	                .contactNo(schoolrequest.getContactNo())
	                .emailId(schoolrequest.getEmailId())
	                .address(schoolrequest.getAddress())
	                .build();
	    }
	private SchoolResponse mapToSchoolResponse(School school) {
        return SchoolResponse.builder()
                .schoolName(school.getSchoolName())
                .contactNo(school.getContactNo())
                .emailId(school.getEmailId())
                .address(school.getAddress())
                .build();
    }
}
