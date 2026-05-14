package com.hireready.serviceimpl;

import com.hireready.dtos.SubmitResponseRequestDTO;
import com.hireready.entities.Question;
import com.hireready.entities.Response;
import com.hireready.entities.Simulation;
import com.hireready.enums.SimulationStatus;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.ResponseRepository;
import com.hireready.services.QuestionService;
import com.hireready.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {
    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    QuestionService questionService;

    // US-11
    @Override
    public Response submit(Simulation simulation, SubmitResponseRequestDTO submitResponseRequestDTO) {

        if (simulation.getStatus() != SimulationStatus.IN_PROGRESS) { // la simulación debe estar en progreso
            throw new ValidationException("Simulation is not in progress");
        }

        if (submitResponseRequestDTO.getQuestionId() == null) {
            throw new ValidationException("questionId is required");
        }
        if (submitResponseRequestDTO.getAudioUrl() == null || submitResponseRequestDTO.getAudioUrl().isBlank()) {
            throw new ValidationException("Audio is required to advance");
        }
        if (submitResponseRequestDTO.getTranscription() == null || submitResponseRequestDTO.getTranscription().isBlank()) {
            throw new ValidationException("Transcription is required");
        }
        if (submitResponseRequestDTO.getDuration() == null || submitResponseRequestDTO.getDuration() <= 0) {
            throw new ValidationException("Duration must be greater than 0");
        }
        if (submitResponseRequestDTO.getDuration() > 120) { // Se tomará como limite de audio de la respuesta 2 min, por motivos de mejor rendimiento
            throw new ValidationException("Duration exceeds the 2-minute maximum");
        }

        List<Question> bankQuestions = questionService.listByBankOrdered(
                simulation.getQuestionBank().getId());
        Question target = null;
        for (Question q : bankQuestions) {
            if (q.getId().equals(submitResponseRequestDTO.getQuestionId())) {
                target = q;
                break;
            }
        }
        if (target == null) { // verificar que la pregunta se del banco (es importante para el analisis ya que compara q vs r)
            throw new ValidationException(
                    "Question id: " + submitResponseRequestDTO.getQuestionId() + " does not belong to the simulation's question bank");
        }

        List<Response> existing = listBySimulationId(simulation.getId());
        for (Response r : existing) {
            if (r.getQuestion() != null && r.getQuestion().getId().equals(target.getId())) {
                throw new ValidationException("Question already answered in this simulation"); // verifica que ya no haya sido respondida
            }
        }

        Response response = new Response(
                null,
                submitResponseRequestDTO.getAudioUrl(),
                submitResponseRequestDTO.getTranscription(),
                submitResponseRequestDTO.getDuration(),
                simulation,
                target,
                null
        );
        return responseRepository.save(response);
    }

    @Override
    public List<Response> listBySimulationId(Long id){
        return responseRepository.findBySimulation_Id(id);
    };
}
