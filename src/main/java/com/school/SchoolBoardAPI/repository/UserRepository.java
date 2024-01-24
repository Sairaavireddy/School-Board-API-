package com.school.SchoolBoardAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

	Optional<User> findByusername(String username);




}
