package com.school.SchoolBoardAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.Schedule;
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{

}
