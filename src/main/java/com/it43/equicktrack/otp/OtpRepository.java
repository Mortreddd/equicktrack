package com.it43.equicktrack.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String>, CrudRepository<Otp, String> {

    Optional<Otp> findByContactNumber(String contactNumber);
    Optional<Otp> findByCode(String code);
    Optional<Otp> findByEmail(String email);

    default boolean codeExists(String code) {
        List<Otp> otps  = findAll();

        return otps.stream()
                .anyMatch((otp) -> otp.getCode() == code);
    }
}
