package com.hireready.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name="authority_id")
    private Authority authority;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Applicant applicant;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Company company;
}