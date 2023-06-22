package com.carspottingapp.service;

import com.carspottingapp.event.listener.RegistrationCompleteEventListener;
import com.carspottingapp.exception.UserAlreadyExistException;
import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.model.CarSpotUserRole;
import com.carspottingapp.model.ICarSpotUser;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.repository.CarSpotUserRepository;
import com.carspottingapp.repository.VerificationTokenRepository;
import com.carspottingapp.service.request.RegistrationRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarSpotUserService implements ICarSpotUser {

    private final CarSpotUserRepository carSpotUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationCompleteEventListener registrationCompleteEventListener;
    private final PasswordResetTokenService passwordResetTokenService;

    @Override
    public List<CarSpotUser> getUsers() {
        return carSpotUserRepository.findAll();
    }

    @Override
    public CarSpotUser registerUser(RegistrationRequest request) {
        Optional<CarSpotUser> carSpotUser = carSpotUserRepository.findByEmail(request.getEmail());
        if(carSpotUser.isPresent()){
            throw new UserAlreadyExistException("User with email"+request.getEmail()+" already exist");
        }
        var newUser = new CarSpotUser();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setUsername(request.getUserName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setCarSpotUserRole(CarSpotUserRole.USER);
        return carSpotUserRepository.save(newUser);
    }

    @Override
    public Optional<CarSpotUser> findByEmail(String email) {
        return carSpotUserRepository.findByEmail(email);
    }


    public void resendVerificationTokenEmail(
            CarSpotUser theUser,
            String applicationUrl,
            VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String verificationUrl = applicationUrl+"/api/register/verifyEmail?token="+verificationToken.getToken();
        registrationCompleteEventListener.sendVerificationEmail(verificationUrl);
        log.info("Click the link to complete your registration : {}", verificationUrl);
    }

    @Override
    public void createPasswordResetTokenForUser(CarSpotUser user, String passwordToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
    }

    @Override
    public CarSpotUser findUserByPasswordToken(String passwordResetToken) {
        return passwordResetTokenService.findUserByPasswordToken(passwordResetToken).get();
    }

    @Override
    public void resetUserPassword(CarSpotUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        carSpotUserRepository.save(user);
    }
}
