package com.projects.RecruiterManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User_Role role;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_head_line")
    private String profileHeadline;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "profiles_id")
    private Profile profile;

    @ManyToMany(mappedBy = "applicants")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Job> appliedJobs = new HashSet<>();

}
