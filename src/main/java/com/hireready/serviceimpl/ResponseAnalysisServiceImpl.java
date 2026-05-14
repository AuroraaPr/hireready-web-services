package com.hireready.serviceimpl;

import com.hireready.entities.Response;
import com.hireready.entities.ResponseAnalysis;
import com.hireready.repositories.ResponseAnalysisRepository;
import com.hireready.services.ResponseAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Arrays.stream;

@Service
public class ResponseAnalysisServiceImpl implements ResponseAnalysisService {
    @Autowired
    ResponseAnalysisRepository responseAnalysisRepository;

    private static final Set<String> FILLER_TOKENS = Set.of(
            "este", "eh", "ehh", "uhm", "bueno", "tipo", "literal", "digamos", "osea"
    );

    // US13

    // heuristica de evaluación provisional (?)
    @Override
    public ResponseAnalysis analyzeAndSave(Response response) {
        String text = response.getTranscription() == null ? "" : response.getTranscription();
        String[] tokens = text.toLowerCase().split("\\s+");
        int wordCount = (int) stream(tokens).filter(s -> !s.isBlank()).count();

        int fillerCount = 0;
        for (String t : tokens) {
            String clean = t.replaceAll("[^a-záéíóúñ]", "");
            if (FILLER_TOKENS.contains(clean)) fillerCount++;
        }

        int relevance = wordCount >= 40 ? 80 : (wordCount >= 15 ? 60 : 40);
        double fillerRatio = wordCount == 0 ? 1.0 : (double) fillerCount / wordCount;
        int clarity = (int) Math.max(40, 100 - (fillerRatio * 200));
        int structure = wordCount >= 30 && text.contains(".") ? 75 : 55;

        String feedback;
        if (clarity >= 75 && relevance >= 75 && structure >= 70) {
            feedback = "Respuesta clara, pertinente y bien estructurada.";
        } else if (clarity < 60) {
            feedback = "Modera el uso de muletillas para mejorar la claridad.";
        } else if (wordCount < 15) {
            feedback = "Respuesta corta: amplía con ejemplos concretos.";
        } else {
            feedback = "Buen punto de partida. Estructura la respuesta con introducción, desarrollo y cierre.";
        }

        ResponseAnalysis analysis = new ResponseAnalysis(
                null, relevance, clarity, structure, feedback, response);
        return responseAnalysisRepository.save(analysis);
    }
}
