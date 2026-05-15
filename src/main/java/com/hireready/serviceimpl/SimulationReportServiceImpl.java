package com.hireready.serviceimpl;

import com.hireready.entities.Response;
import com.hireready.entities.ResponseAnalysis;
import com.hireready.entities.Simulation;
import com.hireready.entities.SimulationReport;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.repositories.SimulationReportRepository;
import com.hireready.services.SimulationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationReportServiceImpl implements SimulationReportService {
    @Autowired
    SimulationReportRepository simulationReportRepository;

    // US13
    @Override
    public SimulationReport generate(Simulation simulation, List<Response> responses) {
        if (responses.isEmpty()) {
            throw new ResourceNotFoundException("Simulation has no responses to report");
        }

        int sumRelevance = 0, sumClarity = 0, sumStructure = 0;
        int totalWords = 0, totalSeconds = 0;
        int analyzed = 0;

        for (Response r : responses) {
            ResponseAnalysis a = r.getResponseAnalysis();
            if (a != null) {
                sumRelevance += a.getRelevanceScore();
                sumClarity += a.getClarityScore();
                sumStructure += a.getStructureScore();
                analyzed++;
            }
            if (r.getTranscription() != null) {
                totalWords += (int) java.util.Arrays.stream(
                        r.getTranscription().split("\\s+")).filter(s -> !s.isBlank()).count();
            }
            if (r.getDuration() != null) totalSeconds += r.getDuration();
        }

        int avgRelevance = analyzed == 0 ? 0 : sumRelevance / analyzed;
        int avgClarity = analyzed == 0 ? 0 : sumClarity / analyzed;
        int avgStructure = analyzed == 0 ? 0 : sumStructure / analyzed;
        int overall = (avgRelevance + avgClarity + avgStructure) / 3;
        int wpm = totalSeconds == 0 ? 0 : (int) Math.round(totalWords * 60.0 /totalSeconds);

        SimulationReport report = new SimulationReport(
                null, avgRelevance, avgClarity, avgStructure, overall, wpm, simulation);
        return simulationReportRepository.save(report);
    }

    // US14
    @Override
    public SimulationReport findBySimulationId(Long simulationId) {
        SimulationReport report = simulationReportRepository.findBySimulation_Id(simulationId);
        if (report == null) {
            throw new ResourceNotFoundException(
                    "Report for simulation id: " + simulationId + " not found");
        }
        return report;
    }
}
