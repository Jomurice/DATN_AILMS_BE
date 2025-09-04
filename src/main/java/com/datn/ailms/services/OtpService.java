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

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository _otpRepository;
    private final OtpGenerator _otpGenerator;
    private final UserService _userService;

    public String createOtp(String userId) {
        String otp = _otpGenerator.generateOtp(6);

        Otp otpEntity = new Otp();
        otpEntity.setUserId(userId);
        otpEntity.setOtpCode(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        _otpRepository.save(otpEntity);
        return otp;
    }

    public boolean validateOtp(String userId, String otp) {
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
        clearOtp(userId);
        return true;
    }

    public void clearOtp(String userId) {
        _otpRepository.deleteById(userId);
    }
}
