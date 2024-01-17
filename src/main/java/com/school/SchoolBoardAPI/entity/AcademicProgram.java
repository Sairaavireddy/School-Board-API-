package com.school.SchoolBoardAPI.entity;

import java.time.LocalTime;
import com.school.SchoolBoardAPI.enums.ProgramType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AcademicProgram {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int programId;
	private ProgramType programtype;
	private String ProgramName;
	private LocalTime beginsAt;
	private LocalTime endsAt;

}
