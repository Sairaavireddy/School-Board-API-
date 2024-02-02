package com.school.SchoolBoardAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.entity.School;
@Repository
public interface AcademicProgramRepository extends JpaRepository<AcademicProgram,Integer> {

	List<AcademicProgram> findByIsDeleted(boolean isDeleted);

//	List<AcademicProgram> findAllBySchool(School school);
//
//	void deleteAllByIsDeleted(boolean b);


}
