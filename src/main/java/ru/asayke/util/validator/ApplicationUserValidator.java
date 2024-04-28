package ru.asayke.util.validator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.service.ApplicationUserService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationUserValidator implements Validator {

    ApplicationUserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ApplicationUser.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ApplicationUser user = (ApplicationUser) target;

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "The username is already taken");
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "The email is already taken");
        }

        if (user.getPassword() == null) {
            errors.rejectValue("password", "Password can't be null");
        }
    }
}
