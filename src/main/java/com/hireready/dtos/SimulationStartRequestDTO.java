package com.hireready.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimulationStartRequestDTO {
    private Long questionBankId;
    private Boolean confirmAbandonPrevious; //para cuando el postulante quiera abandonar la simulación en progreso
}
