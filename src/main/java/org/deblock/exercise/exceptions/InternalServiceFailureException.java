package org.deblock.exercise.exceptions;

public class InternalServiceFailureException extends RuntimeException {

    public InternalServiceFailureException(String message) {

        super(message);
    }
}