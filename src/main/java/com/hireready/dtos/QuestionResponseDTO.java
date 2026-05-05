package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionResponseDTO {
    private Long id;
    private String content;
    private Integer orderIndex;
}
