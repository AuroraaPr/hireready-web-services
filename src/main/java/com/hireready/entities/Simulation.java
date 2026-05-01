package com.hireready.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "simulations")
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "question_bank_id")
    private QuestionBank questionBank;

    public Simulation() {}

    public Long getId() { return id; }
    public boolean isCompleted() { return completed; }
    public QuestionBank getQuestionBank() { return questionBank; }

    public void setId(Long id) { this.id = id; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setQuestionBank(QuestionBank questionBank) { this.questionBank = questionBank; }
}