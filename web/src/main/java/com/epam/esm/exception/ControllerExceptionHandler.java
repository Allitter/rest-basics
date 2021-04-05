package com.epam.esm.exception;

import com.epam.esm.util.ResourceBundleMessage;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {
    private final MessageSource messageSource;

    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException e, Locale locale) {
        String message = messageSource.getMessage(ResourceBundleMessage.ENTITY_NOT_FOUND, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), Collections.singletonList(message));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityExists(EntityAlreadyExistsException e, Locale locale) {
        String message = messageSource.getMessage(ResourceBundleMessage.ENTITY_ALREADY_EXISTS, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), Collections.singletonList(message));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationError.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationError e, Locale locale) {
        List<String> messages = e.getValidations()
                .values().stream()
                .map(validation -> messageSource.getMessage(validation, new Object[]{}, locale))
                .collect(Collectors.toList());

        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), messages);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleException(RuntimeException e, Locale locale) {
        String message = messageSource.getMessage(ResourceBundleMessage.INTERNAL_SERVER_ERROR, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), Collections.singletonList(message));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
