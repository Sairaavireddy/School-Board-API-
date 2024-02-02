package com.school.SchoolBoardAPI.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.entity.ClassHour;
import com.school.SchoolBoardAPI.entity.User;
@Repository
public interface ClassHourRepository extends JpaRepository<ClassHour,Integer>{

	List<ClassHour> findByAcademicProgram(AcademicProgram program);

	void deleteByAcademicProgramProgramId(int programId);

	List<ClassHour> findByBeginsAtBetween(LocalDateTime previousMonday, LocalDateTime previousSaturday);

}
