package ru.asayke.service.interfaces;

import ru.asayke.entity.RegistrationApprovingCode;

import java.util.Optional;

public interface RegistrationCodeService extends ICreatableCode {
    Optional<RegistrationApprovingCode> findByEmail(String email);
}
