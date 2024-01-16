package com.school.SchoolBoardAPI.userrequestdto;



import com.school.SchoolBoardAPI.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserRequest {
	
	
	@NotEmpty(message = "UserName canot be null/empty")
	@Pattern(regexp="^[a-zA-Z0-9]*$", message = "invalid username ")
	private String username;
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must"
			+ " contain at least one letter, one number, one special character")
	private String Password;
	private String firstName;
	private String lastName;
	private String contactNo;
	@NotEmpty(message = "Email cannot be blank/empty")
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "invalid email ")
	private String email;
	private UserRole userRole;


}
