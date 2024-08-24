package ru.asayke.service.interfaces;

import org.springframework.validation.BindingResult;
import ru.asayke.dto.auth.*;
import ru.asayke.entity.ApplicationUser;

import java.util.Map;
import java.util.Optional;

public interface ApplicationUserService {
    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByEmail(String email);

    void register(RegistrationRequest request, BindingResult bindingResult);

    Map<String, String> login(AuthenticationRequest authenticationRequest);

    void startRegistration(EmailRequest startRegistrationRequest);

    void startLogin(EmailRequest startLoginRequest);

    Map<String, String> loginWithEmail(EmailAuthenticationRequest authenticationRequest);

    void startResetPassword(EmailRequest emailRequest);

    void resetPassword(PasswordReseatingDTO passwordDTO);
}
