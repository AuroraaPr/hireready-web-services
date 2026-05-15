package com.hireready.dtos;

import com.hireready.enums.SimulationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationHistoryItemResponseDTO {
    private Long simulationId;
    private String bankName;
    private String companyName;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private SimulationStatus status;
    private Boolean canViewReport; //status COMPLETED >> true
}
