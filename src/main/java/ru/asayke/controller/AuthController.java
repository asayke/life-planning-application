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
import ru.asayke.dto.auth.*;
import ru.asayke.service.interfaces.ApplicationUserService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    ApplicationUserService userService;

    @PostMapping("/start-registration")
    public ResponseEntity<HttpStatus> startRegistration(@RequestBody EmailRequest startRegistrationRequest) {
        userService.startRegistration(startRegistrationRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/start-login")
    public ResponseEntity<HttpStatus> startLogin(@RequestBody EmailRequest startLoginRequest) {
        userService.startLogin(startLoginRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<HttpStatus> register(@RequestBody RegistrationRequest request, BindingResult bindingResult) {
        userService.register(request, bindingResult);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.login(authenticationRequest));
    }

    @PostMapping("/sign-in-with-email")
    public ResponseEntity<Map<String, String>> login(@RequestBody EmailAuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.loginWithEmail(authenticationRequest));
    }

    @PostMapping("/start-reset-password")
    public ResponseEntity<HttpStatus> startResetPassword(@RequestBody EmailRequest emailRequest) {
        userService.startResetPassword(emailRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HttpStatus> resetPassword(@RequestBody PasswordReseatingDTO passwordDTO) {
        userService.resetPassword(passwordDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
