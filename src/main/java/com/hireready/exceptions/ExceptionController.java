package com.hireready.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice

public class ExceptionController {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage validationException(ValidationException e, WebRequest r){
        return new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                r.getDescription(false),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionMessage emptyResultDataAccessException(ResourceNotFoundException e, WebRequest r){
        return new ExceptionMessage(
                HttpStatus.NOT_FOUND.value(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                r.getDescription(false),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionMessage duplicateResourceException(DuplicateResourceException e, WebRequest r){
        return new ExceptionMessage(
                HttpStatus.CONFLICT.value(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                r.getDescription(false),
                LocalDateTime.now()
        );
    }
}
