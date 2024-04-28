package ru.asayke.service;

import org.springframework.validation.BindingResult;
import ru.asayke.dto.AuthenticationRequest;
import ru.asayke.dto.RegistrationRequest;
import ru.asayke.entity.ApplicationUser;

import java.util.Map;
import java.util.Optional;

public interface ApplicationUserService {
    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByEmail(String email);

    void register(RegistrationRequest request, BindingResult bindingResult);

    Map<String, String> login(AuthenticationRequest authenticationRequest);
}
