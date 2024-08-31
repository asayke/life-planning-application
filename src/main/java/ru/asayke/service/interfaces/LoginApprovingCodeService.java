package ru.asayke.service.interfaces;

import ru.asayke.entity.LoginApprovingCode;

import java.util.Optional;

public interface LoginApprovingCodeService extends ICreatableCode {
    Optional<LoginApprovingCode> findByEmail(String email);
}
