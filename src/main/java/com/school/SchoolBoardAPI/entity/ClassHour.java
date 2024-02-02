package com.school.SchoolBoardAPI.entity;

import java.time.LocalDateTime;

import com.school.SchoolBoardAPI.enums.classStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
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
	@Enumerated(EnumType.STRING)
	private classStatus classstatus;

	@ManyToOne
	private AcademicProgram academicProgram;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Subject subject;

}
