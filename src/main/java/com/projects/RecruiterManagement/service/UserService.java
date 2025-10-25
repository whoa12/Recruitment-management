package com.projects.RecruiterManagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.RecruiterManagement.config.JwtGenerator;
import com.projects.RecruiterManagement.model.Job;
import com.projects.RecruiterManagement.model.Profile;
import com.projects.RecruiterManagement.model.User;
import com.projects.RecruiterManagement.model.User_Role;
import com.projects.RecruiterManagement.repository.JobRepository;
import com.projects.RecruiterManagement.repository.ProfileRepository;
import com.projects.RecruiterManagement.repository.UserRepository;
import com.projects.RecruiterManagement.request.CreateJobRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final ProfileRepository profileRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Job createJobOpening(User user, CreateJobRequest req) {
        Job newJob = new Job();
        newJob.setTitle(req.getTitle());
        newJob.setDescription(req.getDescription());
        newJob.setCompanyName(req.getCompanyName());
        newJob.setPostedBy(user);
        newJob.setPostedOn(LocalDateTime.now());
        newJob.setTotalApplications(0);

        return jobRepository.save(newJob);
    }

    @Override
    public Job findJobOpeningDetails(User user, Long jobId) {
        return jobRepository.findById(jobId).
                orElseThrow(() -> new RuntimeException("No job found with id: "+jobId));
    }

    @Override
    public List<User> findUsersList() {
        List<User> userList = userRepository.findByRole(User_Role.ROLE_USER);
        return userList;
    }

    @Override
    public User findApplicantDetails(User user, Long userId) {
        return userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("No user found with id: " +userId));
    }

    @Override
    public List<Job> findJobOpenings() {
        return jobRepository.findAll();
    }

    @Override
    public void applyToJob(User user, Long jobId) {
        Job job = jobRepository.findById(jobId).
                orElseThrow(() -> new RuntimeException("Job not found with id: "+jobId));
        if(job.getApplicants().contains(user)) throw  new RuntimeException("User already applied to this job!");
        job.getApplicants().add(user);
        user.getAppliedJobs().add(job);
        job.setTotalApplications(job.getApplicants().size());
        jobRepository.save(job);
        userRepository.save(user);

    }

    @Override
    public User findByJwt(String jwt) {
        if(jwt!=null && jwt.startsWith("Bearer ")){
            jwt = jwt.substring(7);
        }
        String email = jwtGenerator.getEmailFromJwt(jwt);
        System.out.println(("email gotten:"+ email));

        User user = userRepository.findByEmail(email);
        return user;
    }
    @Override
    public Profile saveProfile(String apiData, String fileName, User user) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(apiData);
        Profile profile = new Profile();

        profile.setName(node.path("name").asText());
        profile.setEmail(node.path("email").asText());
        profile.setPhone(node.path("phone").asText());
        profile.setResumeFileAddress(fileName);
        if (node.has("skills"))
            profile.setSkills(node.get("skills").toString());

        if (node.has("education"))
            profile.setEducation(node.get("education").toString());

        if (node.has("experience"))
            profile.setExperience(node.get("experience").toString());

        user.setProfile(profile);
        userRepository.save(user);

        return profileRepository.save(profile);
    }


}
