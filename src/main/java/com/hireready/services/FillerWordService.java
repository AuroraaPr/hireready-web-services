package com.hireready.services;

import com.hireready.entities.FillerWord;
import com.hireready.entities.Response;
import com.hireready.entities.Simulation;

import java.util.List;

public interface FillerWordService {
    public List<FillerWord> detectAndSave(Simulation simulation, List<Response> responses);
    public List<FillerWord> listBySimulationId(Long simulationId);
}
