package com.school.SchoolBoardAPI.entity;

import java.time.LocalDateTime;

import com.school.SchoolBoardAPI.enums.classStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class ClassHour {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int classHourId;
	private LocalDateTime beginsAt;
	private LocalDateTime endsAt;
	private int roomNo;
	private classStatus classstatus;
	
	@ManyToOne
	private AcademicProgram academicProgram;

}
