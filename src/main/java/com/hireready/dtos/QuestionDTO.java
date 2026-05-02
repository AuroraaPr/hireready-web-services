package com.hireready.dtos;

import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String content;
    private Integer orderIndex;
}
