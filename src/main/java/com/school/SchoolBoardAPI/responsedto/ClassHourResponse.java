package com.school.SchoolBoardAPI.responsedto;

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
public class ClassHourResponse {

	
	private LocalDateTime beginsAt;
	private LocalDateTime endsAt;
	@Enumerated(EnumType.STRING)
	private classStatus classstatus;
	private int roomNo;

}
