package com.hireready.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "careers")
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "career", fetch = FetchType.EAGER)
    private Applicant applicant;

    @OneToMany (mappedBy = "career", fetch = FetchType.EAGER)
    private List<QuestionBankCareer> questionBankCareers;


}
