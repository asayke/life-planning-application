package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.asayke.entity.RegistrationApprovingCode;
import ru.asayke.exception.ApplicationUserValidationException;
import ru.asayke.repository.RegistrationApprovingCodeRepository;
import ru.asayke.service.interfaces.RegistrationCodeService;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationCodeServiceImpl implements RegistrationCodeService {
    RegistrationApprovingCodeRepository registrationApprovingCodeRepository;

    @Override
    public int create(String email) {
        if (registrationApprovingCodeRepository.findByEmail(email).isPresent()) {
            throw new ApplicationUserValidationException("Registration code already exists");
        }

        RegistrationApprovingCode registrationApprovingCode = new RegistrationApprovingCode();

        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        registrationApprovingCode.setCode(code);
        registrationApprovingCode.setEmail(email);

        registrationApprovingCodeRepository.save(registrationApprovingCode);

        return code;
    }

    @Override
    public Optional<RegistrationApprovingCode> findByEmail(String email) {
        return registrationApprovingCodeRepository.findByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        registrationApprovingCodeRepository.deleteById(id);
    }
}
