package com.projects.RecruiterManagement.response;

import com.projects.RecruiterManagement.model.User_Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String jwt;
    private String message;
    private User_Role role;
}
