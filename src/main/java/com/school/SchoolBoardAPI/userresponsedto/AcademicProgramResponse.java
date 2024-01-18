package com.school.SchoolBoardAPI.userresponsedto;

import java.time.LocalDate;

import com.school.SchoolBoardAPI.enums.ProgramType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcademicProgramResponse {
	private int programId;
	private ProgramType programtype;
	private String ProgramName;
	private LocalDate beginsAt;
	private LocalDate endsAt;
}
