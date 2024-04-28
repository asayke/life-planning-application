package ru.asayke.exception;

public class ApplicationUserValidationException extends RuntimeException {
    public ApplicationUserValidationException(String message) {
        super(message);
    }
}
