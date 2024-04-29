package ru.asayke.exception;

public class ApplicationUserNotFoundException extends RuntimeException {
    public ApplicationUserNotFoundException(String message) {
        super(message);
    }
}
