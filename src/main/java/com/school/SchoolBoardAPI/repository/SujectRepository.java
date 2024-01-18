package com.school.SchoolBoardAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.entity.Subject;

@Repository
public interface SujectRepository extends JpaRepository<Subject, Integer>{

	Optional<Subject> findBySubjectName(String name);
	
	

}
