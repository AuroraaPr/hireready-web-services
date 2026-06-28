package com.hireready.serviceimpl;

import com.hireready.entities.QuestionBank;
import com.hireready.entities.Response;
import com.hireready.entities.ResponseAnalysis;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.ResponseAnalysisRepository;
import com.hireready.services.ResponseAnalysisService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;

@Service
public class ResponseAnalysisServiceImpl implements ResponseAnalysisService {
    @Autowired
    ResponseAnalysisRepository responseAnalysisRepository;

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.analysis.model:gpt-4o-mini}")
    private String model;

    @Value("${openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    private RestClient openai;
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    void init() {
        this.openai = RestClient.create(baseUrl);
    }

    private static final Set<String> FILLER_TOKENS = Set.of(
            "este", "eh", "ehh", "uhm", "bueno", "tipo", "literal", "digamos", "osea"
    );

    // US13
    @Override
    public ResponseAnalysis analyzeAndSave(Response response) {
        ResponseAnalysis existing = responseAnalysisRepository.findByResponse_Id(response.getId());
        if (existing != null) return existing;

        String transcription = response.getTranscription() == null ? "" : response.getTranscription();
        String question = response.getQuestion() != null ? response.getQuestion().getContent() : "";

        int relevance, clarity, structure;
        String feedback;

        if (apiKey == null || apiKey.isBlank()) {
            int[] h = heuristicScores(transcription);
            relevance = h[0];
            clarity = h[1];
            structure = h[2];
            feedback = heuristicFeedback(transcription, relevance, clarity, structure);
        } else {
            JsonNode r = callLlm(buildContext(response), question, transcription);
            relevance = clamp(r.path("relevance_score").asInt());
            clarity = clamp(r.path("clarity_score").asInt());
            structure = clamp(r.path("structure_score").asInt());
            feedback = r.path("feedback").asText();
        }

        ResponseAnalysis analysis = new ResponseAnalysis(
                null, relevance, clarity, structure, feedback, response);
        return responseAnalysisRepository.save(analysis);
    }

    @Async
    @Override
    public void analyzeAsync(Response response) {
        try {
            analyzeAndSave(response);
        } catch (Exception e) {
            System.err.println("Async analysis error: " + e.getMessage());
        }
    }

    @Override
    public ResponseAnalysis findByResponseId(Long responseId) {
        return responseAnalysisRepository.findByResponse_Id(responseId);
    }

    // contexto de la entrevista

    private String buildContext(Response response) {
        QuestionBank bank = response.getSimulation() != null
                ? response.getSimulation().getQuestionBank() : null;

        String company = (bank != null && bank.getCompany() != null && bank.getCompany().getName() != null)
                ? bank.getCompany().getName() : "";
        String position = (bank != null && bank.getJobPosition() != null) ? bank.getJobPosition() : "";
        String level = (bank != null && bank.getLevel() != null) ? bank.getLevel() : "";
        String bankName = (bank != null && bank.getName() != null) ? bank.getName() : "";
        String bankDescription = (bank != null && bank.getDescription() != null) ? bank.getDescription() : "";

        return "Empresa: " + (company.isBlank() ? "no especificada" : company) + "\n"
                + "Puesto: " + (position.isBlank() ? "no especificado" : position)
                + (level.isBlank() ? "" : " (nivel: " + level + ")") + "\n"
                + "Banco de preguntas: " + (bankName.isBlank() ? "no especificado" : bankName)
                + (bankDescription.isBlank() ? "" : " - " + bankDescription);
    }

    // Análisis con IA

    private JsonNode callLlm(String context, String question, String transcription) {
        String system = "Eres un evaluador experto de entrevistas laborales. Evalua la respuesta del "
                + "candidato teniendo en cuenta el puesto, el nivel de seniority y la empresa para los "
                + "que esta postulando. Califica de 0 a 100 en tres dimensiones: relevance_score (que tan "
                + "bien y completa responde la pregunta para ESE puesto y nivel), clarity_score (que tan "
                + "claro y fluido se expresa) y structure_score (orden logico: introduccion, desarrollo y "
                + "cierre). Calibra las expectativas segun el nivel: a un puesto Junior no le exijas la "
                + "profundidad de un Senior. Da un feedback breve, especifico y accionable en espanol "
                + "(maximo 3 oraciones) indicando que mejorar para ese rol.";
        String user = context
                + "\n\nPregunta: " + question
                + "\n\nRespuesta del candidato (transcripcion): " + transcription;

        Map<String, Object> schema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "relevance_score", Map.of("type", "integer"),
                        "clarity_score", Map.of("type", "integer"),
                        "structure_score", Map.of("type", "integer"),
                        "feedback", Map.of("type", "string")
                ),
                "required", List.of("relevance_score", "clarity_score", "structure_score", "feedback"),
                "additionalProperties", false
        );
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", system),
                        Map.of("role", "user", "content", user)
                ),
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                                "name", "response_analysis",
                                "strict", true,
                                "schema", schema
                        )
                )
        );

        try {
            JsonNode resp = openai.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);
            String content = resp.path("choices").get(0).path("message").path("content").asText();
            return mapper.readTree(content);
        } catch (Exception e) {
            throw new ValidationException("Error analyzing response with AI: " + e.getMessage());
        }
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }

    // Heurística de respaldo

    private int[] heuristicScores(String text) {
        String[] tokens = text.toLowerCase().split("\\s+");
        int wordCount = 0;
        int fillerCount = 0;
        for (String t : tokens) {
            if (t.isBlank()) continue;
            wordCount++;
            String clean = t.replaceAll("[^a-zaeioun]", "");
            if (FILLER_TOKENS.contains(clean)) fillerCount++;
        }
        int relevance = wordCount >= 40 ? 80 : (wordCount >= 15 ? 60 : 40);
        double fillerRatio = wordCount == 0 ? 1.0 : (double) fillerCount / wordCount;
        int clarity = (int) Math.max(40, 100 - (fillerRatio * 200));
        int structure = wordCount >= 30 && text.contains(".") ? 75 : 55;
        return new int[]{relevance, clarity, structure};
    }

    private String heuristicFeedback(String text, int relevance, int clarity, int structure) {
        int wordCount = 0;
        for (String t : text.toLowerCase().split("\\s+")) if (!t.isBlank()) wordCount++;
        if (clarity >= 75 && relevance >= 75 && structure >= 70) {
            return "Respuesta clara, pertinente y bien estructurada.";
        } else if (clarity < 60) {
            return "Modera el uso de muletillas para mejorar la claridad.";
        } else if (wordCount < 15) {
            return "Respuesta corta: amplia con ejemplos concretos.";
        } else {
            return "Buen punto de partida. Estructura la respuesta con introduccion, desarrollo y cierre.";
        }
    }
}
