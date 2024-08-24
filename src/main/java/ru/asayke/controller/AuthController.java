package ru.asayke.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.asayke.dto.auth.AuthenticationRequest;
import ru.asayke.dto.auth.RegistrationRequest;
import ru.asayke.dto.auth.StartLoginRequest;
import ru.asayke.dto.auth.StartRegistrationRequest;
import ru.asayke.service.interfaces.ApplicationUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    ApplicationUserService userService;

    @PostMapping("/start-registration")
    public ResponseEntity<HttpStatus> startRegistration(@RequestBody StartRegistrationRequest startRegistrationRequest) {
        userService.startRegistration(startRegistrationRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/start-login")
    public ResponseEntity<HttpStatus> startLogin(@RequestBody StartLoginRequest startLoginRequest) {
        userService.startLogin(startLoginRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<HttpStatus> register(@RequestBody RegistrationRequest request, BindingResult bindingResult) {
        userService.register(request, bindingResult);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.login(authenticationRequest));
    }
}
