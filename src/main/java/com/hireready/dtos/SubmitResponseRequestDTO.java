package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmitResponseRequestDTO {
    private Long questionId;
    private String audioUrl;
    private String transcription;
    private Integer duration;
}
