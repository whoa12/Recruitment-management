package com.projects.RecruiterManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "posted_on", nullable = false)
    private LocalDateTime postedOn;

    @Column(name = "total_applications")
    private Integer totalApplications = 0;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posted_by_id")
    private User postedBy;

    @ManyToMany
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<User> applicants = new HashSet<>();

}
