package com.carspottingapp.service;

import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.model.token.PasswordResetToken;
import com.carspottingapp.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    public void createPasswordResetTokenForUser(CarSpotUser carSpotUser, String passwordToken){
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, carSpotUser);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public String validatePasswordResetToken(String verifiedToken){
        PasswordResetToken token = passwordResetTokenRepository.findByResetToken(verifiedToken);
        if(token == null){
            return "Invalid password reset token";
        }
        CarSpotUser user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if((token.getTokenExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            return "Link already expired";
        }
        return "valid";
    }

    public Optional<CarSpotUser> findUserByPasswordToken(String passwordToken){
        return Optional.ofNullable(passwordResetTokenRepository.findByResetToken(passwordToken).getUser());
    }
}
