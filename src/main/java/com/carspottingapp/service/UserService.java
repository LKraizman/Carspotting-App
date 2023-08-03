package com.carspottingapp.service;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.model.User;
import com.carspottingapp.model.UserActions;
import com.carspottingapp.model.response.UserResponse;
import com.carspottingapp.model.response.UserResponseWithToken;
import com.carspottingapp.repository.AccessTokenRepository;
import com.carspottingapp.repository.UserRepository;
import com.carspottingapp.service.request.PasswordRequest;
import com.carspottingapp.service.request.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserActions {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationService userAuthenticationService;
    private final AccessTokenRepository tokenRepository;

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(UserResponse::new).toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    @Override
    public UserResponse findById(Long id) {
        return userRepository.findById(id).map(UserResponse::new).orElseThrow(InvalidIdException::new);
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

    public UserResponseWithToken changeUserInformation(
            Long id,
            UserRegistrationRequest updateUserInformationRequest)
            throws IOException {
        User existUser = userRepository.findById(id).orElseThrow(InvalidIdException::new);
        existUser.setUsername(updateUserInformationRequest.getUserName());
        existUser.setFirstName(updateUserInformationRequest.getFirstName());
        existUser.setLastName(updateUserInformationRequest.getLastName());
        userRepository.save(existUser);
        userAuthenticationService.refreshUserToken(existUser);
        String updatedToken = tokenRepository.findAllValidTokenByUser(existUser.getId()).get(0).getToken();
        return new UserResponseWithToken(existUser, updatedToken);
    }

    public String userPasswordChangeResponse(PasswordRequest passwordRequestUtil, Long id) {
        if (!Objects.equals(passwordRequestUtil.getOldPassword(), passwordRequestUtil.getOldPasswordRepeat())) {
            return "The passwords don't match";
        }
        User user = userRepository.findById(id).get();
        if (!oldPasswordIsValid(user, passwordRequestUtil.getOldPassword())) {
            return "Incorrect user password";
        }
        changePassword(user, passwordRequestUtil.getNewPassword());
        return "Password changed successfully";
    }
}
