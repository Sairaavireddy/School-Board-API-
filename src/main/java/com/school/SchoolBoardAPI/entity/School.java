package com.school.SchoolBoardAPI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int schoolId;
	@OneToOne
	private Schedule schedule;
	
	private String schoolName;
	private long contactNo;
	private String emailId;
	private String address;
	


}
