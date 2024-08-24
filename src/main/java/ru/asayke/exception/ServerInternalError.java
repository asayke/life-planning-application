package ru.asayke.exception;

public class ServerInternalError extends RuntimeException {
    public ServerInternalError(String message) {
        super(message);
    }
}
