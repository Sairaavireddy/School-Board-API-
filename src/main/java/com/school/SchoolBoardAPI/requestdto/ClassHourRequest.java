package com.school.SchoolBoardAPI.requestdto;

import java.time.LocalDateTime;

import com.school.SchoolBoardAPI.enums.classStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ClassHourRequest {
	
	private LocalDateTime beginsAt;
	private LocalDateTime endsAt;
	private int roomNo;
	@Enumerated(EnumType.STRING)
	private classStatus classstatus;

}
