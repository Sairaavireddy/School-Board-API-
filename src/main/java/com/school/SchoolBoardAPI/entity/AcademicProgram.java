package com.school.SchoolBoardAPI.entity;

import java.time.LocalDate;
import java.util.List;

import com.school.SchoolBoardAPI.enums.ProgramType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AcademicProgram {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "program_id")
	private int programId;
	private ProgramType programtype;
	private String ProgramName;
	private LocalDate beginsAt;
	private LocalDate endsAt;
	private boolean isDeleted;
	@ManyToOne
	School school;
	
	@ManyToMany
	List<Subject> Slist;
	
    @ManyToMany
	private List<User> Userlist;
    
    @OneToMany(mappedBy = "academicProgram")
    private List<ClassHour> classHourlist;

}
