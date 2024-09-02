package ru.asayke.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.asayke.dto.ApplicationUserDto;
import ru.asayke.service.interfaces.ApplicationUserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    ApplicationUserService applicationUserService;

    @GetMapping
    public ResponseEntity<ApplicationUserDto> getUserById(Principal principal) {
        return ResponseEntity.ok(applicationUserService.getMyProfile(principal.getName()));
    }
}
