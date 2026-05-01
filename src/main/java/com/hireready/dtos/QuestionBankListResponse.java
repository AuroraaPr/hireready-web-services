package com.hireready.dtos;

import java.util.List;
import java.util.ArrayList;

public class QuestionBankListResponse {

    private Long id;
    private String title;
    private String jobPosition;
    private String level;

    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED
    private List<String> actions;

    private String companyName;
    private Integer questionCount;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getJobPosition() { return jobPosition; }
    public String getLevel() { return level; }
    public String getStatus() { return status; }
    public List<String> getActions() { return actions; }
    public String getCompanyName() { return companyName; }
    public Integer getQuestionCount() { return questionCount; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setJobPosition(String jobPosition) { this.jobPosition = jobPosition; }
    public void setLevel(String level) { this.level = level; }
    public void setStatus(String status) { this.status = status; }
    public void setActions(List<String> actions) { this.actions = actions; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setQuestionCount(Integer questionCount) { this.questionCount = questionCount; }

}
