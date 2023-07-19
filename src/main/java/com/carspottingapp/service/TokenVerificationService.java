package com.carspottingapp.service;

import com.carspottingapp.model.User;
import com.carspottingapp.model.token.IVerificationToken;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordResetTokenService passwordResetTokenService;

    public VerificationToken isTokenExist(String token) {
        VerificationToken verifiedToken = verificationTokenRepository.findByToken(token);
        if (verifiedToken.getUser().getIsEnabled()) {
            return null;
        }
        return verifiedToken;
    }

    @Override
    public void saveUserVerificationToken(User user, String verificationToken) {
        var verifiedToken = new VerificationToken(verificationToken, user);
        verificationTokenRepository.save(verifiedToken);
    }

    @Override
    public String validateToken(String verifiedToken) {
        VerificationToken token = verificationTokenRepository.findByToken(verifiedToken);
        if (token == null) {
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getTokenExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(token);
            return "Token already expired.";
        }
        user.setIsEnabled(true);
        userRepository.save(user);
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
