package ru.asayke.service.interfaces;

import ru.asayke.entity.PasswordReseatingCode;

public interface PasswordReseatingCodeService {
   //TODO make another interface with #createCode
    void createCode(String email);

    PasswordReseatingCode findByEmail(String email);
}
