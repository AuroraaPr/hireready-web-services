package com.hireready.dtos;

import com.hireready.enums.SimulationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulationResponseDTO {
    private Long simulationId;
    private Long applicantId;
    private Long questionBankId;
    private String questionBankName;
    @Enumerated(EnumType.STRING)
    private SimulationStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
