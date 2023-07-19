package com.carspottingapp.service;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.exception.UserAlreadyExistException;
import com.carspottingapp.exception.UserNotFoundException;
import com.carspottingapp.model.User;
import com.carspottingapp.model.UserRole;
import com.carspottingapp.model.UserActions;
import com.carspottingapp.model.response.UserResponse;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.repository.UserRepository;
import com.carspottingapp.service.request.UserDataRequest;
import com.carspottingapp.utils.EmailContent;
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
public class UserService implements UserActions {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailSender emailSender;
    private final EmailContent emailContent;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(UserDataRequest request) {
        Optional<User> carSpotUser = userRepository.findByEmail(request.getEmail());
        if (carSpotUser.isPresent()) {
            throw new UserAlreadyExistException("User with email" + request.getEmail() + " already exist");
        }
        var newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setUsername(request.getUserName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setUserRole(UserRole.USER);
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResponse findById(Long id) {
        return userRepository.findById(id).map(UserResponse::new).orElseThrow(InvalidIdException::new);
    }

    public void resendVerificationTokenEmail(
            User theUser,
            String applicationUrl,
            VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String verificationUrl = applicationUrl + "/api/register/verifyEmail?token=" + verificationToken.getToken();
        emailSender.sendEmail(verificationUrl, theUser, emailContent.getACCOUNT_VERIFICATION_TITLE());
        log.info("Click the link to complete your registration : {}", verificationUrl);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
    }

    @Override
    public User findUserByPasswordToken(String passwordResetToken) {
        return passwordResetTokenService.findUserByPasswordToken(passwordResetToken)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean oldPasswordIsValid(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public UserResponse changeUserInformation(Long id, UserDataRequest request) {
        User existUser = userRepository.findById(id).orElseThrow(InvalidIdException::new);
        existUser.setUsername(request.getUserName());
        existUser.setFirstName(request.getFirstName());
        existUser.setLastName(request.getLastName());
        User updatedUser = userRepository.save(existUser);
        return new UserResponse(updatedUser);
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id).map(UserResponse::new).
                orElseThrow(InvalidIdException::new);
    }
}
