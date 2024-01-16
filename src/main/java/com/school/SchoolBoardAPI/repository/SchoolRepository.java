package com.school.SchoolBoardAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.School;

@Repository
public interface SchoolRepository extends JpaRepository<School,Integer> {

}
