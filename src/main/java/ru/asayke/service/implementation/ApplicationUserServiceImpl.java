package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.asayke.dto.auth.AuthenticationRequest;
import ru.asayke.dto.auth.RegistrationRequest;
import ru.asayke.dto.auth.StartLoginRequest;
import ru.asayke.dto.auth.StartRegistrationRequest;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.LoginApprovingCode;
import ru.asayke.entity.RegistrationApprovingCode;
import ru.asayke.exception.ApplicationUserNotFoundException;
import ru.asayke.exception.ApplicationUserValidationException;
import ru.asayke.exception.ServerInternalError;
import ru.asayke.repository.ApplicationUserRepository;
import ru.asayke.security.JwtTokenProvider;
import ru.asayke.service.interfaces.ApplicationUserService;
import ru.asayke.service.interfaces.RegistrationCodeService;
import ru.asayke.util.ErrorsUtils;
import ru.asayke.util.MapperUtils;
import ru.asayke.util.validator.RegistrationValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationUserServiceImpl implements ApplicationUserService {

    ApplicationUserRepository userRepository;

    RegistrationCodeService registrationCodeService;

    LoginApprovingCodeServiceImpl loginApprovingCodeService;

    RegistrationValidator registrationValidator;

    JwtTokenProvider jwtTokenProvider;

    AuthenticationManager authenticationManager;

    BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<ApplicationUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<ApplicationUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void register(RegistrationRequest request, BindingResult bindingResult) {
        registrationValidator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            ErrorsUtils.returnErrorsToClient(bindingResult);
        }

        RegistrationApprovingCode registrationApprovingCode = registrationCodeService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ServerInternalError("Something went wrong"));

        if (!registrationApprovingCode.getEmail().equals(request.getEmail()) || !registrationApprovingCode.getCode().equals(request.getCode())) {
            throw new ApplicationUserValidationException("Invalid registration code");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        ApplicationUser applicationUser = MapperUtils.enrichUserFromRegistrationDto(request);

        userRepository.save(applicationUser);
    }

    @Override
    public Map<String, String> login(AuthenticationRequest authenticationRequest) {
        ApplicationUser applicationUser = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new ApplicationUserNotFoundException("User not found"));

        LoginApprovingCode byEmail = loginApprovingCodeService.findByEmail(applicationUser.getEmail())
                .orElseThrow(() -> new ServerInternalError("Login code not found"));

        if (!authenticationRequest.getCode().equals(byEmail.getCode())) {
            throw new ApplicationUserValidationException("Login code is wrong");
        }

        String username = authenticationRequest.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword()));
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        String token = jwtTokenProvider.createToken(username, List.of(user.getRole()));

        Map<String, String> response = Map.of("token", token);

        return response;
    }

    @Override
    public void startRegistration(StartRegistrationRequest startRegistrationRequest) {
        if (userRepository.findByEmail(startRegistrationRequest.getEmail()).isPresent()) {
            throw new ApplicationUserValidationException(String.format("User with email %s already exists", startRegistrationRequest.getEmail()));
        }

        registrationCodeService.createCode(startRegistrationRequest.getEmail());
    }

    @Override
    public void startLogin(StartLoginRequest startLoginRequest) {
        loginApprovingCodeService.createCode(startLoginRequest.getEmail());
    }
}
