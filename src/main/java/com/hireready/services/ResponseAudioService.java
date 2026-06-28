package com.hireready.services;

import com.hireready.entities.ResponseAudio;

public interface ResponseAudioService {
    public void add(ResponseAudio responseAudio);
    public ResponseAudio findByResponseId(Long responseId);
}
