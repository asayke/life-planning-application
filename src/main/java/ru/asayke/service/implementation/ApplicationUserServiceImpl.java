package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.asayke.dto.AuthenticationRequest;
import ru.asayke.dto.RegistrationRequest;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.repository.ApplicationUserRepository;
import ru.asayke.security.JwtTokenProvider;
import ru.asayke.service.ApplicationUserService;
import ru.asayke.util.ErrorsUtil;
import ru.asayke.util.MapperUtil;
import ru.asayke.util.validator.RegistrationValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationUserServiceImpl implements ApplicationUserService {

    ApplicationUserRepository userRepository;

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
    public void register(RegistrationRequest request, BindingResult bindingResult) {
        registrationValidator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnErrorsToClient(bindingResult);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        ApplicationUser applicationUser = MapperUtil.enrichUserFromRegistrationDto(request);

        userRepository.save(applicationUser);
    }

    @Override
    public Map<String, String> login(AuthenticationRequest authenticationRequest) {
            String username = authenticationRequest.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword()));
            ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

            String token = jwtTokenProvider.createToken(username, List.of(user.getRole()));

            Map<String, String> response = Map.of("token", token);

            return response;
        }
}
