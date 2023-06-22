package com.carspottingapp.model.token;

import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.service.request.RegistrationRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface IVerificationToken {

    void saveUserVerificationToken(CarSpotUser carSpotUser, String verificationToken);

    String validateToken(String verifiedToken);

    VerificationToken generateNewVerificationToken(String oldToken);

    String validatePasswordResetToken(String passwordResetToken);
}
