package com.hireready.services;

public interface TranscriptionService {
    public String transcribe(byte[] audioBytes, String filename);
}
