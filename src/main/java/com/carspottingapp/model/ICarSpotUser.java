package com.carspottingapp.model;

import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.service.request.RegistrationRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ICarSpotUser {
    List<CarSpotUser> getUsers();
    CarSpotUser registerUser(RegistrationRequest request);
    Optional<CarSpotUser> findByEmail(String email);
    void createPasswordResetTokenForUser(CarSpotUser user, String passwordToken);

    CarSpotUser findUserByPasswordToken(String passwordResetToken);

    void resetUserPassword(CarSpotUser user, String newPassword);
}
