package com.school.SchoolBoardAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.AcademicProgram;
import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

	Optional<User> findByusername(String username);
	
	List<User>  findByIsDeleted(boolean b);

	List<User> findByIsDeletedAndUserRoleIn(boolean b, List<UserRole> asList);


	Optional<User> findUserByUserRole(UserRole admin);


	List<User> findBySchool(School school);


	List<User> findByUserRoleAndListAcademicPrograms(UserRole valueOf, AcademicProgram program);



	






}
