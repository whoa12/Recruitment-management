package com.projects.RecruiterManagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    @Column(name = "resume_file_address")
    private String resumeFileAddress;

    @Column(length = 1000)
    private String skills;

    @Column(length = 1000)
    private String education;

    @Column(length = 2000)
    private String experience;
}
