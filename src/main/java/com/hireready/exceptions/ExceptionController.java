package com.hireready.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice

public class ExceptionController {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage validationException(ValidationException e, WebRequest r) {
        return build(HttpStatus.BAD_REQUEST.value(), e, r);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage resourceNotFound(ResourceNotFoundException e, WebRequest r) {
        return build(HttpStatus.NOT_FOUND.value(), e, r);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionMessage duplicateResource(DuplicateResourceException e, WebRequest r) {
        return build(HttpStatus.CONFLICT.value(), e, r);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage forbidden(ForbiddenException e, WebRequest r) {
        return build(HttpStatus.FORBIDDEN.value(), e, r);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage badCredentials(BadCredentialsException e, WebRequest r) {
        return build(HttpStatus.UNAUTHORIZED.value(), e, r);
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage disabled(DisabledException e, WebRequest r) {
        return build(HttpStatus.FORBIDDEN.value(), e, r); // US-19: user.enabled=false
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionMessage accessDenied(AccessDeniedException e, WebRequest r) {
        return build(HttpStatus.FORBIDDEN.value(), e, r); // ruta exige rol que no tienes
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage authentication(AuthenticationException e, WebRequest r) {
        return build(HttpStatus.UNAUTHORIZED.value(), e, r);
    }

    private ExceptionMessage build(int status, Exception e, WebRequest r) {
        return new ExceptionMessage(
                status,
                e.getClass().getSimpleName(),
                e.getMessage(),
                r.getDescription(false),
                LocalDateTime.now()
        );
    }
}
