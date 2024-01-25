package com.school.SchoolBoardAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.entity.User;
import com.school.SchoolBoardAPI.enums.UserRole;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

	Optional<User> findByusername(String username);





	int findByUserId(int adminId);

	Optional<User> findUserByUserRole(UserRole admin);





	User findByUserRole(UserRole teacher);





//	Optional<User> findByIdAndUserRole(int id, UserRole teacher);




}
