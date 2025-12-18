package com.datn.ailms.repositories.userRepo;

import com.datn.ailms.model.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findByUserIdAndOtpCode(String userId, String otpCode);

}
