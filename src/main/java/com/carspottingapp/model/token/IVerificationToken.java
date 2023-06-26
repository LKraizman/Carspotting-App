package com.carspottingapp.model.token;

import com.carspottingapp.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IVerificationToken {

    void saveUserVerificationToken(User user, String verificationToken);

    String validateToken(String verifiedToken);

    VerificationToken generateNewVerificationToken(String oldToken);

    String validatePasswordResetToken(String passwordResetToken);
}
