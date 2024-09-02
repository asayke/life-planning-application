package ru.asayke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asayke.entity.PasswordReseatingCode;

import java.util.Optional;

@Repository
public interface PasswordReseatingCodeRepository extends JpaRepository<PasswordReseatingCode, Long> {
    Optional<PasswordReseatingCode> findByEmail(String email);

    boolean existsByEmail(String email);
}
