package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.asayke.entity.LoginApprovingCode;
import ru.asayke.exception.ApplicationUserValidationException;
import ru.asayke.repository.LoginApprovingCodeRepository;
import ru.asayke.service.interfaces.LoginApprovingCodeService;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginApprovingCodeServiceImpl implements LoginApprovingCodeService {
    LoginApprovingCodeRepository loginApprovingCodeRepository;

    @Override
    public int create(String email) {
        if (loginApprovingCodeRepository.findByEmail(email).isPresent()) {
            throw new ApplicationUserValidationException("Login code already exists");
        }

        LoginApprovingCode loginApprovingCode = new LoginApprovingCode();

        loginApprovingCode.setEmail(email);

        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        loginApprovingCode.setCode(code);
        loginApprovingCodeRepository.save(loginApprovingCode);

        return code;
    }

    @Override
    public Optional<LoginApprovingCode> findByEmail(String email) {
        return loginApprovingCodeRepository.findByEmail(email);
    }
}
