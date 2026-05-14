package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionScoreDTO {
    private Long questionId;
    private String questionContent;
    private String bankName;
    private Integer averageScore;
    private Integer responseCount;
}
