package ru.asayke.util.validator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.asayke.dto.RegistrationRequest;
import ru.asayke.repository.ApplicationUserRepository;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationValidator implements Validator {
    ApplicationUserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationRequest registrationRequest = (RegistrationRequest) target;

        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            errors.rejectValue("username", "The username is already taken");
        }

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            errors.rejectValue("email", "The email is already taken");
        }

        if (registrationRequest.getPassword() == null) {
            errors.rejectValue("password", "Password can't be null");
        }
    }
}
