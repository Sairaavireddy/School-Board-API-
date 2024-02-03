package com.school.SchoolBoardAPI.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.entity.ClassHour;
import com.school.SchoolBoardAPI.entity.User;
@Repository
public interface ClassHourRepository extends JpaRepository<ClassHour,Integer>{

	List<ClassHour> findByAcademicProgram(AcademicProgram program);

	void deleteByAcademicProgramProgramId(int programId);

	

	Optional<ClassHour> findTopByOrderByClassHourIdDesc();

	List<ClassHour> findByAcademicProgramAndBeginsAtBetween(AcademicProgram academicProgram, LocalDateTime atStartOfDay,
			LocalDateTime plusDays);

	List<ClassHour> findByBeginsAtBeforeAndEndsAtAfter(LocalDateTime currentTime, LocalDateTime currentTime2);
	
//	List<ClassHour> findByAcademicProgramAndBeginsAtBetween(AcademicProgram programId, LocalDateTime from, LocalDateTime to);

}
