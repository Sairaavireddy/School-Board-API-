package com.school.SchoolBoardAPI.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.repository.SchoolRepository;
import com.school.SchoolBoardAPI.service.SchoolService;
import com.school.SchoolBoardAPI.utility.ResponseStructure;
@Service
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	private SchoolRepository schoolrepository;

	@Override
	public ResponseEntity<ResponseStructure<School>> SaveSchool(School school) {
		School school2 = schoolrepository.save(school);
		ResponseStructure<School> rs=new ResponseStructure<>();
		rs.setStatus(HttpStatus.CREATED.value());
		rs.setMessage("school object created Sucessfully");
		rs.setData(school2);

		return new ResponseEntity<ResponseStructure<School>>(rs,HttpStatus.CREATED);
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

//	@Override
//	public String deleteSchool() {
//		// TODO Auto-generated method stub
//		schoolrepository.deleteById(101);
//		return "deleted Sucessfully";
//	}


}
