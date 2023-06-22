package com.carspottingapp.service;

import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.model.token.IVerificationToken;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.repository.CarSpotUserRepository;
import com.carspottingapp.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenVerificationService implements IVerificationToken {
    private final VerificationTokenRepository verificationTokenRepository;
    private final CarSpotUserRepository carSpotUserRepository;
    private final PasswordResetTokenService passwordResetTokenService;

    @Override
    public void saveUserVerificationToken(CarSpotUser carSpotUser, String verificationToken) {
        var verifiedToken = new VerificationToken(verificationToken,carSpotUser);
        verificationTokenRepository.save(verifiedToken);
    }

    @Override
    public String validateToken(String verifiedToken) {
        VerificationToken token = verificationTokenRepository.findByToken(verifiedToken);
        if(token == null){
            return "Invalid verification token";
        }
        CarSpotUser user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if((token.getTokenExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(token);
            return "Token already expired.";
        }
        user.setIsEnabled(true);
        carSpotUserRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validatePasswordResetToken(String passwordResetToken) {
        return passwordResetTokenService.validatePasswordResetToken(passwordResetToken);
    }
}
