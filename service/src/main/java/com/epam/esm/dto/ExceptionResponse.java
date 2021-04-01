package com.epam.esm.dto;

import java.util.Objects;
import java.util.StringJoiner;

public class ExceptionResponse {
    private final int code;
    private final String message;

    public ExceptionResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ExceptionResponse.class.getSimpleName() + "[", "]")
                .add("code=" + code)
                .add("Message='" + message + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionResponse that = (ExceptionResponse) o;
        return code == that.code && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }
}
