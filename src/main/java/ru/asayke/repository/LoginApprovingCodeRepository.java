package ru.asayke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asayke.entity.LoginApprovingCode;

import java.util.Optional;

@Repository
public interface LoginApprovingCodeRepository extends JpaRepository<LoginApprovingCode, Long> {
    Optional<LoginApprovingCode> findByEmail(String email);
}
