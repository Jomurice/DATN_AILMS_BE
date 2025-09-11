package com.datn.ailms.services;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.model.dto.request.EmailRequestDto;

import com.datn.ailms.model.dto.request.PasswordRequestDto;
import com.datn.ailms.model.entities.Otp;

import com.datn.ailms.model.entities.account_entities.User;
import com.datn.ailms.repositories.userRepo.OtpRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserService _userService;
    private final MailService _mailService;
    private final OtpService _otpService;
    private final OtpRepository _otpRepository;

    public void sendResetCode(EmailRequestDto request) {
        User user = _userService.getUserEntityByEmail(request.getEmail());
        String otpCode = _otpService.createOtp(user.getId());

        String subject = "Khôi phục mật khẩu";
        String content = "Mã OTP của bạn là: " + otpCode +
                "\nVui lòng nhập mã OTP này để đổi mật khẩu." +
                "\nLưu ý: Mã OTP chỉ có hiệu lực trong vòng 5 phút.";

        _mailService.sendMail(user.getEmail(), subject, content);
    }

    public boolean verifyOTP(PasswordRequestDto request) {
        User user = _userService.getUserEntityByEmail(request.getEmail());
        Otp otpEntity = _otpRepository.findByUserIdAndOtpCode(user.getId(), request.getOtpCode()).orElseThrow(() -> new AppException(ErrorCode.OTP_INVALID));
        return true;
    }


    public void ResetPassword(PasswordRequestDto request) {
        User user = _userService.getUserEntityByEmail(request.getEmail());

        boolean isValidOtp = _otpService.validateOtp(user.getId(), request.getOtpCode());
        if (!isValidOtp) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        _userService.updateUserPassword(user.getId(), request.getNewPassword());
        _otpService.clearOtp(user.getId());
    }

}
