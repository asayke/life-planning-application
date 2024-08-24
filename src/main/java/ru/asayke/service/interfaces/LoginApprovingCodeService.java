package ru.asayke.service.interfaces;

import ru.asayke.entity.LoginApprovingCode;

import java.util.Optional;

public interface LoginApprovingCodeService {
    void createCode(String email);

    Optional<LoginApprovingCode> findByEmail(String email);
}
