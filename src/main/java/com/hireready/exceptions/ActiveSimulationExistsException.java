package com.hireready.exceptions;

public class ActiveSimulationExistsException extends RuntimeException {
    public ActiveSimulationExistsException(String message) {
        super(message);
    }
}
