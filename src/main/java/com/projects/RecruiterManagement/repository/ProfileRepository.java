package com.projects.RecruiterManagement.repository;

import com.projects.RecruiterManagement.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
