package ru.asayke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asayke.entity.RegistrationApprovingCode;

import java.util.Optional;

@Repository
public interface RegistrationApprovingCodeRepository extends JpaRepository<RegistrationApprovingCode, Long> {
    Optional<RegistrationApprovingCode> findByEmail(String email);
}
