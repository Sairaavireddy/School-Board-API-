package com.school.SchoolBoardAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.SchoolBoardAPI.entity.School;
import com.school.SchoolBoardAPI.enums.UserRole;

@Repository
public interface SchoolRepository extends JpaRepository<School,Integer> {

	List<School> findByIsDeleted(boolean b);

//	School findByIsDeletedNotInUser(boolean isDeleted,UserRole userrole);

}
