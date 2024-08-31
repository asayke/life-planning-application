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
import ru.asayke.dto.auth.*;
import ru.asayke.dto.kafka.EmailEvent;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.LoginApprovingCode;
import ru.asayke.entity.PasswordReseatingCode;
import ru.asayke.entity.RegistrationApprovingCode;
import ru.asayke.exception.ApplicationUserNotFoundException;
import ru.asayke.exception.ApplicationUserValidationException;
import ru.asayke.exception.ServerInternalError;
import ru.asayke.repository.ApplicationUserRepository;
import ru.asayke.security.JwtTokenProvider;
import ru.asayke.service.implementation.kafka.KafkaMessagingService;
import ru.asayke.service.interfaces.ApplicationUserService;
import ru.asayke.service.interfaces.PasswordReseatingCodeService;
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

    //todo
    RegistrationCodeService registrationCodeService;

    LoginApprovingCodeServiceImpl loginApprovingCodeService;

    PasswordReseatingCodeService passwordReseatingCodeService;
    //todo

    RegistrationValidator registrationValidator;

    JwtTokenProvider jwtTokenProvider;

    AuthenticationManager authenticationManager;

    BCryptPasswordEncoder passwordEncoder;

    KafkaMessagingService kafkaMessagingService;

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
        } else {
            loginApprovingCodeService.deleteById(byEmail.getId());
        }

        String username = authenticationRequest.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword()));
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        String token = jwtTokenProvider.createToken(username, List.of(user.getRole()));

        Map<String, String> response = Map.of("token", token);

        return response;
    }

    @Override
    public Map<String, String> loginWithEmail(EmailAuthenticationRequest authenticationRequest) {
        ApplicationUser applicationUser = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new ApplicationUserNotFoundException("User not found"));

        LoginApprovingCode byEmail = loginApprovingCodeService.findByEmail(applicationUser.getEmail())
                .orElseThrow(() -> new ServerInternalError("Login code not found"));

        if (!authenticationRequest.getCode().equals(byEmail.getCode())) {
            throw new ApplicationUserValidationException("Login code is wrong");
        } else {
            loginApprovingCodeService.deleteById(byEmail.getId());
        }

        String username = applicationUser.getUsername();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword()));
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        String token = jwtTokenProvider.createToken(username, List.of(user.getRole()));

        Map<String, String> response = Map.of("token", token);

        return response;
    }

    @Override
    public void startResetPassword(EmailRequest emailRequest) {
        int resetCode = registrationCodeService.create(emailRequest.getEmail());

        EmailEvent kafkaDTO = new EmailEvent(
                emailRequest.getEmail(),
                "Password reseating",
                String.format("Your registration code is %s", resetCode)
        );

        kafkaMessagingService.sendMessage(kafkaDTO);
    }

    @Override
    public void resetPassword(PasswordReseatingDTO passwordDTO) {
        ApplicationUser applicationUser = userRepository.findByEmail(passwordDTO.getEmail())
                .orElseThrow(() -> new ApplicationUserNotFoundException("User not found"));

        PasswordReseatingCode byEmail = passwordReseatingCodeService.findByEmail(passwordDTO.getEmail());

        if (byEmail.getCode().equals(passwordDTO.getCode())) {
            applicationUser.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            passwordReseatingCodeService.deleteById(byEmail.getId());
        } else {
            throw new ApplicationUserValidationException("Reset code is wrong");
        }
    }

    @Override
    public void startRegistration(EmailRequest startRegistrationRequest) {
        if (userRepository.findByEmail(startRegistrationRequest.getEmail()).isPresent()) {
            throw new ApplicationUserValidationException(String.format("User with email %s already exists", startRegistrationRequest.getEmail()));
        }

        int registrationCode = registrationCodeService.create(startRegistrationRequest.getEmail());

        EmailEvent kafkaDTO = new EmailEvent(
                startRegistrationRequest.getEmail(),
                "Your registration code",
                String.format("Your registration code is %s", registrationCode)
        );

        kafkaMessagingService.sendMessage(kafkaDTO);
    }

    @Override
    public void startLogin(EmailRequest startLoginRequest) {
        int loginCode = loginApprovingCodeService.create(startLoginRequest.getEmail());

        EmailEvent kafkaDTO = new EmailEvent(
                startLoginRequest.getEmail(),
                "Your confirmation code",
                String.format("Your login code is %s", loginCode)
        );

        kafkaMessagingService.sendMessage(kafkaDTO);
    }
}
