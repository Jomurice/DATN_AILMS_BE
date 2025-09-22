package com.datn.ailms.services;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.model.entities.Otp;
import com.datn.ailms.repositories.userRepo.OtpRepository;
import com.datn.ailms.utils.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository _otpRepository;
    private final OtpGenerator _otpGenerator;
    private final UserService _userService;

    public String createOtp(UUID userId) {
        String otp = _otpGenerator.generateCodeNumbers(6);

        Otp otpEntity = new Otp();
        otpEntity.setUserId(userId);
        otpEntity.setOtpCode(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(1));

        _otpRepository.save(otpEntity);
        return otp;
    }

    public boolean validateOtp(UUID userId, String otp) {
        Otp otpEntity = _otpRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.OTP_NOT_EXISTED)
        );
        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            clearOtp(userId);
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }
        if (!otpEntity.getOtpCode().equals(otp)) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        return true;
    }

    public void clearOtp(UUID userId) {
        _otpRepository.deleteById(userId);
    }
}
