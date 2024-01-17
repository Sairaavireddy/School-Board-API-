package com.school.SchoolBoardAPI.userresponsedto;
import com.school.SchoolBoardAPI.enums.UserRole;

import jakarta.persistence.Column;
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
@Builder
@Data
public class UserResponse {
	private int userId;
	private String username;
	private String firstName;
	private String lastName;
	private String contactNo;
	private String email;
	private UserRole userRole;
	public Boolean isDeleted;
	}
