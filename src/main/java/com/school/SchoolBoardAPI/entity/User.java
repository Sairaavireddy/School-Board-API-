package com.school.SchoolBoardAPI.entity;

import java.util.List;

import com.school.SchoolBoardAPI.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(unique = true)
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String contactNo;
	@Column(unique = true)
	private String email;
//	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	public boolean isDeleted;
	
	@ManyToOne
	School school;
	
	@ManyToMany(mappedBy = "Userlist")
	private List<AcademicProgram> aprogramlist;
	
	@ManyToOne
	private Subject subject;
	
	@OneToMany(mappedBy ="user")
	List<ClassHour> classHourlist;

}
