package com.it43.equicktrack.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String>, CrudRepository<Otp, String> {

    Optional<Otp> findByUserId(Long userId);
    Optional<Otp> findByEmail(String email);

}
