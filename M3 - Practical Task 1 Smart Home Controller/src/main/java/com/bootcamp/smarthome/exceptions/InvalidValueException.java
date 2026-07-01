package com.bootcamp.smarthome.exceptions;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String field, Object value, String constraint) {
        super("Invalid value for field: " + field + ". Value: " + value + ". Constraint: " + constraint);
    }
}
