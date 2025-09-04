package com.datn.ailms.services;

import com.datn.ailms.exceptions.AppException;
import com.datn.ailms.exceptions.ErrorCode;
import com.datn.ailms.model.dto.request.EmailRequestDto;
import com.datn.ailms.model.dto.request.PasswordRequestDto;
import com.datn.ailms.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserService _userService;
    private final MailService _mailService;
    private final OtpService _otpService;


    public void sendResetCode(EmailRequestDto request) {
        User user = _userService.getUserEntityByEmail(request.getEmail());
        String otpCode = _otpService.createOtp(user.getId());

        String subject = "Khôi phục mật khẩu";
        String content = "Mã OTP của bạn là: " + otpCode +
                "\nVui lòng nhập mã OTP này để đổi mật khẩu." +
                "\nLưu ý: Mã OTP chỉ có hiệu lực trong vòng 5 phút.";

        _mailService.sendMail(user.getEmail(), subject, content);
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
    }

}
