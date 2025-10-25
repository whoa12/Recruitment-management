package com.projects.RecruiterManagement.repository;

import com.projects.RecruiterManagement.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job,Long> {

}
