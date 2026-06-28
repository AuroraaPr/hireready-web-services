package com.hireready.services;

import com.hireready.dtos.*;
import com.hireready.entities.ResponseAudio;
import com.hireready.entities.Simulation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SimulationService {
    public SimulationResponseDTO start(Long applicantUserId, SimulationStartRequestDTO simulationStartRequestDTO);
    public ContinueSimulationResponseDTO continueLatest(Long applicantUserId);
    public SubmitResponseResponseDTO submitResponse(Long applicantUserId, Long simulationId,
                                                    Long questionId, Integer duration, MultipartFile audio);
    public ExitSimulationResponseDTO exit(Long applicantUserId, Long simulationId);
    public FinalizeSimulationResponseDTO finalize(Long applicantUserId, Long simulationId);
    public SimulationReportFullResponseDTO getReport(Long applicantUserId, Long simulationId);
    public List<SimulationHistoryItemResponseDTO> listHistory(Long applicantUserId);
    public ResponseAudio getResponseAudio(Long applicantUserId, Long simulationId, Long responseId);
}
