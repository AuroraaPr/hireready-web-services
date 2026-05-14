package com.hireready.serviceimpl;

import com.hireready.entities.FillerWord;
import com.hireready.entities.Response;
import com.hireready.entities.Simulation;
import com.hireready.repositories.FillerWordRepository;
import com.hireready.services.FillerWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FillerWordServiceImpl implements FillerWordService {
    @Autowired
    FillerWordRepository fillerWordRepository;

    private static final Set<String> FILLER_TOKENS = Set.of(
            "este", "eh", "ehh", "uhm", "bueno", "tipo", "literal", "digamos", "osea"
    );

    // US-13
    @Override
    public List<FillerWord> detectAndSave(Simulation simulation, List<Response> responses) {
        Map<String, Integer> counts = new HashMap<>();
        for (Response r : responses) {
            if (r.getTranscription() == null) continue;
            for (String raw : r.getTranscription().toLowerCase().split("\\s+")) {
                String clean = raw.replaceAll("[^a-záéíóúñ]", "");
                if (FILLER_TOKENS.contains(clean)) {
                    counts.merge(clean, 1, Integer::sum);
                }
            }
        }
        List<FillerWord> saved = new ArrayList<>();
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            FillerWord fw = new FillerWord(null, e.getKey(), e.getValue(), simulation);
            saved.add(fillerWordRepository.save(fw));
        }
        return saved;
    }

    // US14
    @Override
    public List<FillerWord> listBySimulationId(Long simulationId) {
        return fillerWordRepository.findBySimulation_Id(simulationId);
    }
}
