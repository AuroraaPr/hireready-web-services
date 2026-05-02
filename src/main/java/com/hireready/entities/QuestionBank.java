package com.hireready.entities;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "question_banks")
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String jobPosition;

    private String level;

    @OneToMany(mappedBy = "questionBank", fetch = FetchType.EAGER)
    private List<Simulation> simulations;

    @OneToMany(mappedBy = "questionBank", fetch = FetchType.EAGER)
    private List<QuestionBankCareer> questionBankCareers;

    @ManyToOne
    @JoinColumn (name="company_id")
    private Company company;

    @OneToMany(mappedBy = "questionBank", fetch = FetchType.EAGER)
    private List<Question> questions;
}
