package ru.asayke.exception;

public class ScheduledEventFieldDoesNotExists extends RuntimeException {
    public ScheduledEventFieldDoesNotExists(String message) {
        super(message);
    }
}
