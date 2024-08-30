package com.school.SchoolBoardAPI.requestdto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelRequestDto {
	private LocalDate fromDate;
	private LocalDate toDate;
	private String filePath;

}
