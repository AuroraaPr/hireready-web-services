package com.hireready.serviceimpl;

import com.hireready.entities.Question;
import com.hireready.entities.Response;
import com.hireready.entities.ResponseAudio;
import com.hireready.entities.Simulation;
import com.hireready.enums.SimulationStatus;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.ResponseRepository;
import com.hireready.services.QuestionService;
import com.hireready.services.ResponseAudioService;
import com.hireready.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {
    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private ResponseAudioService responseAudioService;

    @Autowired
    QuestionService questionService;

    // US-11
    @Override
    public Response submit(Simulation simulation, Long questionId, Integer duration,
                           String transcription, byte[] audioBytes, String contentType) {

        if (simulation.getStatus() != SimulationStatus.IN_PROGRESS) { // la simulación debe estar en progreso
            throw new ValidationException("Simulation is not in progress");
        }

        if (questionId == null) {
            throw new ValidationException("questionId is required");
        }
        if (audioBytes == null || audioBytes.length == 0) {
            throw new ValidationException("Audio is required to advance");
        }
        if (duration == null || duration <= 0) {
            throw new ValidationException("Duration must be greater than 0");
        }
        if (duration > 120) { // limite de 2 min por respuesta
            throw new ValidationException("Duration exceeds the 2-minute maximum");
        }

        List<Question> bankQuestions = questionService.listByBankOrdered(
                simulation.getQuestionBank().getId());
        Question target = null;
        for (Question q : bankQuestions) {
            if (q.getId().equals(questionId)) {
                target = q;
                break;
            }
        }
        if (target == null) { // verificar que la pregunta se del banco (es importante para el analisis ya que compara q vs r)
            throw new ValidationException(
                    "Question id: " + questionId + " does not belong to the simulation's question bank");
        }

        List<Response> existing = listBySimulationId(simulation.getId());
        for (Response r : existing) {
            if (r.getQuestion() != null && r.getQuestion().getId().equals(target.getId())) {
                throw new ValidationException("Question already answered in this simulation"); // verifica que ya no haya sido respondida
            }
        }

        Response response = new Response(
                null,
                transcription == null ? "" : transcription,
                duration,
                simulation,
                target,
                null
        );
        response = responseRepository.save(response);

        ResponseAudio audio = new ResponseAudio(
                response.getId(),
                audioBytes,
                contentType != null ? contentType : "audio/webm"
        );
        responseAudioService.add(audio);

        return response;
    }

    @Override
    public List<Response> listBySimulationId(Long id){
        return responseRepository.findBySimulation_Id(id);
    };

    @Override
    public boolean existsInSimulation(Long responseId, Long simulationId) {
        return responseRepository.existsByIdAndSimulation_Id(responseId, simulationId);
    }
}
