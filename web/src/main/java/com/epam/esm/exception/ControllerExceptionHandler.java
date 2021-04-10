package com.epam.esm.exception;

import com.epam.esm.util.ResourceBundleMessage;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityExists(EntityAlreadyExistsException e, Locale locale) {
        String message = messageSource.getMessage(ResourceBundleMessage.ENTITY_ALREADY_EXISTS, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException e, Locale locale) {
        List<String> messages = e.getValidationFails()
                .values().stream()
                .map(validationFail -> messageSource.getMessage(validationFail, new Object[]{}, locale))
                .collect(Collectors.toList());

        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), messages);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> handleParseException(HttpMessageNotReadableException e, Locale locale) {
        String message = messageSource.getMessage(ResourceBundleMessage.BAD_REQUEST, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleException(RuntimeException e, Locale locale) {
        String message = messageSource.getMessage(ResourceBundleMessage.INTERNAL_SERVER_ERROR, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
