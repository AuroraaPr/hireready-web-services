package com.hireready.serviceimpl;

import com.hireready.entities.ResponseAudio;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.repositories.ResponseAudioRepository;
import com.hireready.services.ResponseAudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseAudioServiceImpl implements ResponseAudioService {
    @Autowired
    private ResponseAudioRepository responseAudioRepository;

    @Override
    public void add(ResponseAudio responseAudio) {
        responseAudioRepository.save(responseAudio);
    }

    @Override
    public ResponseAudio findByResponseId(Long responseId) {
        return responseAudioRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Audio for response id: " + responseId + " not found"));
    }
}
