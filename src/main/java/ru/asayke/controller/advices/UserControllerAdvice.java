package ru.asayke.controller.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.asayke.controller.UserController;
import ru.asayke.exception.ApplicationUserValidationException;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {

    @ExceptionHandler(ApplicationUserValidationException.class)
    public ResponseEntity<String> handleApplicationUserValidationException(ApplicationUserValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}