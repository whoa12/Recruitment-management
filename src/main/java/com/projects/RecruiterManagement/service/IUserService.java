package com.projects.RecruiterManagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.projects.RecruiterManagement.model.Job;
import com.projects.RecruiterManagement.model.Profile;
import com.projects.RecruiterManagement.model.User;
import com.projects.RecruiterManagement.request.CreateJobRequest;

import java.util.List;

public interface IUserService {
    Job createJobOpening(User user, CreateJobRequest req);
    Job findJobOpeningDetails(User user, Long jobId);
    List<User> findUsersList();
    User findApplicantDetails(User user, Long userId);
    List<Job> findJobOpenings();
    void applyToJob(User user,Long jobId);

    User findByJwt(String jwt);
     Profile saveProfile(String apiData, String fileName, User user) throws JsonProcessingException;

}
