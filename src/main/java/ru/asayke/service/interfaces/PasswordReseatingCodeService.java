package ru.asayke.service.interfaces;

import ru.asayke.entity.PasswordReseatingCode;

public interface PasswordReseatingCodeService extends ICreatableCode {
    PasswordReseatingCode findByEmail(String email);

    void deleteById(Long id);
}
