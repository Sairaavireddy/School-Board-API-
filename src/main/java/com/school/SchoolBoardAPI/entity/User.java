package com.school.SchoolBoardAPI.entity;

import java.util.List;

import com.school.SchoolBoardAPI.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@ManyToOne
	School school;
	@Column(unique = true)
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String contactNo;
	@Column(unique = true)
	private String email;
	private UserRole userRole;
	public Boolean isDeleted;
	
	@OneToMany(mappedBy ="Userlist")
	private List<AcademicProgram> Aprogramlist;
	@ManyToOne
	private Subject subject;

}
