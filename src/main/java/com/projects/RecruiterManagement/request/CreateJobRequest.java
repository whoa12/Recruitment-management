package com.projects.RecruiterManagement.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobRequest {
    private String title;
    private String description;
    private String companyName;
}
