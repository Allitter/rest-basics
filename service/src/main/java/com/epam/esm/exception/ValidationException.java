package com.epam.esm.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> validations;

    public ValidationException(Map<String, String> validations) {
        this.validations = new HashMap<>(validations);
    }

    public Map<String, String> getValidations() {
        return Collections.unmodifiableMap(validations);
    }
}
