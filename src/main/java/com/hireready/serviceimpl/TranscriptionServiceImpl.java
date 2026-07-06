package com.hireready.serviceimpl;

import com.hireready.exceptions.ValidationException;
import com.hireready.services.TranscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

@Service
public class TranscriptionServiceImpl implements TranscriptionService {
    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.transcription.model:gpt-4o-mini-transcribe}")
    private String model;

    private final RestClient openai = RestClient.create("https://api.openai.com/v1");

    @Override
    public String transcribe(byte[] audioBytes, String filename) {
        if (apiKey == null || apiKey.isBlank()) {
            return "Error in transcription: configure OPENAI_API_KEY";
        }

        final String name = (filename != null && filename.contains(".")) ? filename : "audio.webm";

        ByteArrayResource fileResource = new ByteArrayResource(audioBytes) {
            @Override
            public String getFilename() {
                return name;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("model", model);
        body.add("file", fileResource);

        try {
            JsonNode resp = openai.post()
                    .uri("/audio/transcriptions")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);

            return resp.path("text").asText();
        } catch (Exception e) {
            throw new ValidationException("Error transcribing audio: " + e.getMessage());
        }
    }
}
