package com.projects.RecruiterManagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.projects.RecruiterManagement.model.Job;
import com.projects.RecruiterManagement.model.Profile;
import com.projects.RecruiterManagement.model.User;
import com.projects.RecruiterManagement.model.User_Role;
import com.projects.RecruiterManagement.repository.UserRepository;
import com.projects.RecruiterManagement.request.CreateJobRequest;
import com.projects.RecruiterManagement.service.IUserService;
import com.projects.RecruiterManagement.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final StorageService storageService;
    private final UserRepository userRepository;
    private final IUserService userService;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> handleFileUpload(@RequestHeader("Authorization") String jwt,@RequestParam("file") MultipartFile file, User user) throws JsonProcessingException {
        String apiJson = storageService.store(file);
        user = userService.findByJwt(jwt);

        Profile savedProfile = userService.saveProfile(apiJson, file.getOriginalFilename(),user);

        Map<String, Object> response = new HashMap<>();
        response.put("fileName", file.getOriginalFilename());
        response.put("message", "Upload successful");
        response.put("Profile", savedProfile);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/job")
    public ResponseEntity<Job> createJobOpening(@RequestHeader("Authorization") String jwt,@RequestBody CreateJobRequest req)
            throws AccessDeniedException {
        User user = userService.findByJwt(jwt);
        if(user.getRole()!=User_Role.ROLE_ADMIN){
            throw new AccessDeniedException("Applicants cannot create job opening!");
        }
        Job createdJob = userService.createJobOpening(user, req);
        return  new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping("/admin/job/{job_id}")
    public ResponseEntity<Job> fetchJobInformation(@RequestHeader("Authorization") String jwt, @PathVariable("job_id") Long jobId)
            throws AccessDeniedException {
        User user = userService.findByJwt(jwt);
        if(user.getRole()!=User_Role.ROLE_ADMIN){
            throw new AccessDeniedException("Applicants cannot access jobs!");
        }
        Job fetchedJob = userService.findJobOpeningDetails(user,jobId);
        return  new ResponseEntity<>(fetchedJob, HttpStatus.OK);
    }

    @GetMapping("/admin/applicants")
    public ResponseEntity<List<User>> getUsersList(@RequestHeader("Authorization") String jwt)
            throws AccessDeniedException {
         User user = userService.findByJwt(jwt);
        if(user.getRole()!=User_Role.ROLE_ADMIN){
            throw new AccessDeniedException("Applicants cannot access the list!");
        }
        List<User> userList = userService.findUsersList();
        return  new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/admin/applicants/{applicant_id}")
    public ResponseEntity<User> fetchUserInformation(@RequestHeader("Authorization") String jwt, @PathVariable("applicant_id") Long applicantId)
            throws AccessDeniedException {
        User user = userService.findByJwt(jwt);
        if(user.getRole()!=User_Role.ROLE_ADMIN){
            throw new AccessDeniedException("Users can't fetch information!");
        }
        User applicantsInfo = userService.findApplicantDetails(user, applicantId);
        return  new ResponseEntity<>(applicantsInfo, HttpStatus.OK);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getJobs()
            {

        List<Job> jobsList = userService.findJobOpenings();
        return  new ResponseEntity<>(jobsList, HttpStatus.OK);
    }


    @PostMapping("/jobs/apply")
    public ResponseEntity<String> applyToJob(@RequestHeader("Authorization") String jwt, @RequestParam Long job_id)
            throws AccessDeniedException {
        User user = userService.findByJwt(jwt);
        if(user.getRole()!= User_Role.ROLE_USER){
            throw new AccessDeniedException("Admins can't apply to jobs!");
        }
        userService.applyToJob(user,job_id);
        return  new ResponseEntity<>("Applied to job successfully!", HttpStatus.OK);
    }
}
