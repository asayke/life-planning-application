package ru.asayke.service.interfaces;

import org.springframework.validation.BindingResult;
import ru.asayke.dto.auth.AuthenticationRequest;
import ru.asayke.dto.auth.RegistrationRequest;
import ru.asayke.dto.auth.StartLoginRequest;
import ru.asayke.dto.auth.StartRegistrationRequest;
import ru.asayke.entity.ApplicationUser;

import java.util.Map;
import java.util.Optional;

public interface ApplicationUserService {
    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByEmail(String email);

    void register(RegistrationRequest request, BindingResult bindingResult);

    Map<String, String> login(AuthenticationRequest authenticationRequest);

    void startRegistration(StartRegistrationRequest startRegistrationRequest);

    void startLogin(StartLoginRequest startLoginRequest);
}
