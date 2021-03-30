package com.epam.esm.controller.exception;

import com.epam.esm.controller.dto.ExceptionResponse;
import com.epam.esm.exception.EntityNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class ControllerExceptionHandler {
    private final MessageSource messageSource;

    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException e, Locale locale) {
        String message = messageSource.getMessage(HttpStatus.NOT_FOUND.name(), new Object[0], locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
