package com.hireready.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SimulationStartRequestDTO {
    @NotNull
    private Long questionBankId;
    private boolean forceStart; //para cuando el postulante quiera abandonar la simulación en progreso
}
