package com.projects.RecruiterManagement.repository;

import com.projects.RecruiterManagement.model.User;
import com.projects.RecruiterManagement.model.User_Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
   User findByEmail(String email);
   List<User> findByRole(User_Role role);

}
