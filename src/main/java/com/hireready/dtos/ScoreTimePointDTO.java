package com.hireready.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreTimePointDTO {
    private LocalDateTime completedAt;
    private Integer overallScore;
    private String bankName;
}
