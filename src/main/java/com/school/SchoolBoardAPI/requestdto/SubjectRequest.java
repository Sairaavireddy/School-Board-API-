package com.school.SchoolBoardAPI.requestdto;

import java.util.List;

import com.school.SchoolBoardAPI.entity.Subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubjectRequest {
	List<String> subjectName;

}
