package com.hireready.serviceimpl;

import com.hireready.entities.FillerWord;
import com.hireready.entities.Response;
import com.hireready.entities.Simulation;
import com.hireready.repositories.FillerWordRepository;
import com.hireready.services.FillerWordService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FillerWordServiceImpl implements FillerWordService {
    @Autowired
    FillerWordRepository fillerWordRepository;

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

    // US-13
    @Override
    public List<FillerWord> detectAndSave(Simulation simulation, List<Response> responses) {
        StringBuilder sb = new StringBuilder();
        int n = 1;
        for (Response r : responses) {
            if (r.getTranscription() == null || r.getTranscription().isBlank()) continue;
            sb.append("Respuesta ").append(n++).append(": ")
                    .append(r.getTranscription().trim()).append("\n");
        }
        String corpus = sb.toString().trim();

        Map<String, Integer> counts;
        if (corpus.isBlank()) {
            counts = Map.of();
        } else if (apiKey == null || apiKey.isBlank()) {
            counts = detectWithRegex(corpus);
        } else {
            counts = detectWithLlm(corpus);
        }

        List<FillerWord> saved = new ArrayList<>();
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() == null || e.getValue() <= 0) continue;
            FillerWord fw = new FillerWord(null, e.getKey(), e.getValue(), simulation);
            saved.add(fillerWordRepository.save(fw));
        }
        return saved;
    }

    private Map<String, Integer> detectWithLlm(String corpus) {
        String system = "Eres un analizador de muletillas en entrevistas en espanol. Te paso las "
                + "respuestas habladas (transcripciones) de un candidato. Identifica SOLO las palabras "
                + "o frases usadas como muletilla o relleno: titubeos (eh, mmm), conectores vacios, "
                + "repeticiones sin contenido y coletillas. NO las cuentes cuando se usan de forma "
                + "legitima como contenido. Ejemplos: en 'claro que tengo experiencia', 'claro' NO es "
                + "muletilla; en 'eh, claro, este...', si lo es. En 'o sea, queria decir' es muletilla; "
                + "en 'es decir, X es Y' es un conector valido, no lo cuentes. Agrupa las variantes "
                + "alargadas de un mismo titubeo bajo una sola forma base (por ejemplo 'eh', 'ehh' y "
                + "'ehhh' cuentan todas como 'eh'; 'mm' y 'mmm' como 'mmm') y suma sus repeticiones. "
                + "Devuelve cada muletilla (en minuscula) con su conteo total sumado en TODO el texto. "
                + "Si una muletilla aparece en varias respuestas, suma todas sus apariciones en un solo "
                + "conteo. Si no hay muletillas, devuelve una lista vacia.";

        Map<String, Object> schema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "fillers", Map.of(
                                "type", "array",
                                "items", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "word", Map.of("type", "string"),
                                                "count", Map.of("type", "integer")
                                        ),
                                        "required", List.of("word", "count"),
                                        "additionalProperties", false
                                )
                        )
                ),
                "required", List.of("fillers"),
                "additionalProperties", false
        );
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", system),
                        Map.of("role", "user", "content", corpus)
                ),
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                                "name", "filler_words",
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
            JsonNode parsed = mapper.readTree(content);

            Map<String, Integer> counts = new LinkedHashMap<>();
            for (JsonNode f : parsed.path("fillers")) {
                String word = f.path("word").asText("").trim().toLowerCase();
                int count = f.path("count").asInt(0);
                if (!word.isBlank() && count > 0) {
                    counts.merge(word, count, Integer::sum);
                }
            }
            return counts;
        } catch (Exception e) {
            return detectWithRegex(corpus);
        }
    }

    private static String canonical(String word) {
        String x = normalize(word).trim().replaceAll("\\s+", " ");
        if (x.matches("e+h+")) return "eh";
        if (x.matches("a+h+")) return "ah";
        if (x.matches("u+h+")) return "uh";
        if (x.matches("h+m+")) return "hmm";
        if (x.matches("m{2,}")) return "mmm";
        if (x.equals("osea")) return "o sea";
        return x;
    }

    private static String normalize(String s) {
        String x = Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD);
        return x.replaceAll("\\p{M}+", "");
    }

    // Respaldo

    private static final List<Pattern> SAFE_PATTERNS = List.of(
            Pattern.compile("\\be+h+\\b"),
            Pattern.compile("\\ba+h+\\b"),
            Pattern.compile("\\bu+h+\\b"),
            Pattern.compile("\\bh+m+\\b"),
            Pattern.compile("\\bmm+\\b"),
            Pattern.compile("\\bo\\s+sea\\b"),
            Pattern.compile("\\bosea\\b"),
            Pattern.compile("\\ben\\s+plan\\b"),
            Pattern.compile("\\bcomo\\s+que\\b"),
            Pattern.compile("\\bponle\\b"),
            Pattern.compile("\\bque\\s+se\\s+yo\\b")
    );

    private Map<String, Integer> detectWithRegex(String corpus) {
        String text = normalize(corpus);
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Pattern p : SAFE_PATTERNS) {
            Matcher m = p.matcher(text);
            while (m.find()) {
                String key = canonical(m.group());
                if (!key.isBlank()) counts.merge(key, 1, Integer::sum);
            }
        }
        return counts;
    }

    // US14
    @Override
    public List<FillerWord> listBySimulationId(Long simulationId) {
        return fillerWordRepository.findBySimulation_Id(simulationId);
    }
}
