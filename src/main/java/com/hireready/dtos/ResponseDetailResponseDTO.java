package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDetailResponseDTO {
    private Long responseId;
    private Integer orderIndex;
    private String questionContent;
    private String audioUrl;
    private String transcription;
    private Integer duration;
    private Integer relevanceScore;
    private Integer clarityScore;
    private Integer structureScore;
    private String feedback;
}
