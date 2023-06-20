package com.carspottingapp.service;

import com.carspottingapp.exception.UserAlreadyExistException;
import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.model.CarSpotUserRole;
import com.carspottingapp.model.ICarSpotUser;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.repository.CarSpotUserRepository;
import com.carspottingapp.repository.VerificationTokenRepository;
import com.carspottingapp.service.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarSpotUserService implements ICarSpotUser {

    private final CarSpotUserRepository carSpotUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

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
}
