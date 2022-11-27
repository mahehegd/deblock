package org.deblock.exercise.exceptions;

public class InternalClientErrorException extends RuntimeException {

    public InternalClientErrorException(String message) {

        super(message);
    }
}