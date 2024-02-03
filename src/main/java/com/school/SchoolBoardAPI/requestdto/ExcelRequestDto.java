package com.school.SchoolBoardAPI.requestdto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
	public LocalDateTime fromDateatTime(LocalTime midnight) {
		
		return LocalDateTime.of(LocalDate.now(), midnight);
	}
	public LocalDateTime toDateatTime(LocalTime midnight) {
		return LocalDateTime.of(LocalDate.now(), midnight).plusDays(1);
	}

}
