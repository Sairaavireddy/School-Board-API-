package com.school.SchoolBoardAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
@Repository
public interface AcademicProgramRepository extends JpaRepository<AcademicProgram,Integer> {

}
