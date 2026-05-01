package com.hireready.entities;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "question_banks")
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String jobPosition;

    private String level;

    @OneToMany(mappedBy = "questionBank", cascade = CascadeType.ALL)
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "question_bank_careers",
            joinColumns = @JoinColumn(name = "question_bank_id"),
            inverseJoinColumns = @JoinColumn(name = "career_id")
    )
    private List<Career> careers = new java.util.ArrayList<>();

    public QuestionBank() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getJobPosition() { return jobPosition; }
    public String getLevel() { return level; }
    public List<Question> getQuestions() { return questions; }
    public List<Career> getCareers() { return careers; }
    public Company getCompany() { return company; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setJobPosition(String jobPosition) { this.jobPosition = jobPosition; }
    public void setLevel(String level) { this.level = level; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public void setCareers(List<Career> careers) { this.careers = careers; }
    public void setCompany(Company company) { this.company = company; }

}
