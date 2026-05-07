package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionBankDetailResponseDTO {

    private Long id;
    private String title;
    private String jobPosition;
    private String level;
    private List<QuestionDetailResponseDTO> questions;

}