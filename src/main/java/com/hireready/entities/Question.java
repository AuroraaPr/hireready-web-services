package com.hireready.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private QuestionBank questionBank;

    public Question(){}

    public Long getId(){return id;}
    public String getContent(){return content;}
    public Integer getOrderIndex() {return orderIndex;}
    public QuestionBank getQuestionBank(){return questionBank;}

    public void setId(Long id){this.id = id;}
    public void setContent(String content){this.content = content;}
    public void setOrderIndex(Integer orderIndex) {this.orderIndex = orderIndex;}
    public void setQuestionBank(QuestionBank questionBank){this.questionBank = questionBank;}
}
