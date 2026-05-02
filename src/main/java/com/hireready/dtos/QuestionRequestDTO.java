package com.hireready.dtos;

import lombok.Data;

@Data
public class QuestionRequestDTO {
    private String content;
    private Integer orderIndex;
}
