package ru.asayke.service.interfaces;

import ru.asayke.entity.RegistrationApprovingCode;

import java.util.Optional;

public interface RegistrationCodeService {
    void createCode(String email);

    Optional<RegistrationApprovingCode> findByEmail(String email);
}
