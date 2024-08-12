package com.it43.equicktrack.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByToken(UUID token);
}
