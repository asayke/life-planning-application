package ru.asayke;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.asayke.dto.auth.AuthenticationRequest;
import ru.asayke.dto.auth.EmailAuthenticationRequest;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.LoginApprovingCode;
import ru.asayke.exception.ApplicationUserNotFoundException;
import ru.asayke.exception.ApplicationUserValidationException;
import ru.asayke.exception.ServerInternalError;
import ru.asayke.repository.ApplicationUserRepository;
import ru.asayke.security.JwtTokenProvider;
import ru.asayke.service.implementation.ApplicationUserServiceImpl;
import ru.asayke.service.implementation.LoginApprovingCodeServiceImpl;
import ru.asayke.service.interfaces.PasswordReseatingCodeService;
import ru.asayke.service.interfaces.RegistrationCodeService;
import ru.asayke.util.validator.RegistrationValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUserServiceImplTest {

    @Mock
    private ApplicationUserRepository userRepository;

    @Mock
    private RegistrationCodeService registrationCodeService;

    @Mock
    private LoginApprovingCodeServiceImpl loginApprovingCodeService;

    @Mock
    private PasswordReseatingCodeService passwordReseatingCodeService;

    @Mock
    private RegistrationValidator registrationValidator;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private ApplicationUserServiceImpl applicationUserService;

    @Test
    void findByUsername_shouldReturnOptionalApplicationUser_whenUserExists() {
        String username = "testUser";
        ApplicationUser user = new ApplicationUser();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<ApplicationUser> result = applicationUserService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByUsername_shouldReturnEmptyOptional_whenUserDoesNotExist() {
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<ApplicationUser> result = applicationUserService.findByUsername(username);

        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_shouldReturnOptionalApplicationUser_whenUserExist() {
        String email = "test@example.com";
        ApplicationUser user = new ApplicationUser();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<ApplicationUser> result = applicationUserService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByEmail_shouldReturnEmptyOptional_whenUserDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<ApplicationUser> result = applicationUserService.findByEmail(email);

        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_shouldReturnOptionalApplicationUser_whenUserExists() {
        String email = "test@example.com";
        ApplicationUser user = new ApplicationUser();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<ApplicationUser> result = applicationUserService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByEmail_shouldReturnEmptyOptional_whenUserDoesNotExists() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<ApplicationUser> result = applicationUserService.findByEmail(email);

        assertFalse(result.isPresent());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("testUser");
        authenticationRequest.setPassword("password");
        authenticationRequest.setCode(123456);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(ApplicationUserNotFoundException.class, () -> applicationUserService.login(authenticationRequest));
    }

    @Test
    void loginWithEmail_shouldThrowException_whenLoginCodeIsInvalid() {
        EmailAuthenticationRequest authenticationRequest = new EmailAuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setCode(123456);
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setEmail("test@example.com");
        applicationUser.setUsername("testUser");
        LoginApprovingCode byEmail = new LoginApprovingCode();
        byEmail.setCode(789012);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(applicationUser));
        when(loginApprovingCodeService.findByEmail(anyString())).thenReturn(Optional.of(byEmail));

        assertThrows(ApplicationUserValidationException.class, () -> applicationUserService.loginWithEmail(authenticationRequest));
    }

    @Test
    void loginWithEmail_shouldThrowException_whenLoginCodeIsNotFound() {
        EmailAuthenticationRequest authenticationRequest = new EmailAuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setCode(123456);
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setEmail("test@example.com");
        applicationUser.setUsername("testUser");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(applicationUser));
        when(loginApprovingCodeService.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ServerInternalError.class, () -> applicationUserService.loginWithEmail(authenticationRequest));
    }
}
