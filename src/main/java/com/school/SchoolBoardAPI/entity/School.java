package com.school.SchoolBoardAPI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class School {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@OneToOne
 private int schoolId;
 private String schoolName;
 private long contactNo;
 private String emailId;
 private String address;
 
 
}
