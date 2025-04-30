package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asayke.entity.PasswordReseatingCode;
import ru.asayke.exception.ApplicationUserValidationException;
import ru.asayke.repository.PasswordReseatingCodeRepository;
import ru.asayke.service.interfaces.PasswordReseatingCodeService;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordReseatingCodeServiceImpl implements PasswordReseatingCodeService {
    PasswordReseatingCodeRepository passwordReseatingCodeRepository;

    @Override
    @Transactional
    public int create(String email) {
        if (!existByEmail(email)) {

        PasswordReseatingCode passwordReseatingCode = new PasswordReseatingCode();

        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        passwordReseatingCode.setCode(code);
        passwordReseatingCode.setEmail(email);

        passwordReseatingCodeRepository.save(passwordReseatingCode);

        return code;
        } else {
            throw new RuntimeException("Reset code already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordReseatingCode findByEmail(String email) {
        return passwordReseatingCodeRepository.findByEmail(email).get();
    }

    @Transactional(readOnly = true)
    public boolean existByEmail(String email) {
        return passwordReseatingCodeRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        passwordReseatingCodeRepository.deleteById(id);
    }
}
