package com.hireready.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name="question_bank_id")
    private QuestionBank questionBank;

    @OneToMany (mappedBy = "question", fetch = FetchType.EAGER)
    private List<Response> responses;
}
