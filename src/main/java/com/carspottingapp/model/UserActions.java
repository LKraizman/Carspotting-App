package com.carspottingapp.model;

import com.carspottingapp.service.request.UserDataRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserActions {

    List<User> getUsers();

    User registerUser(UserDataRequest request);

    Optional<User> findByEmail(String email);

    void createPasswordResetTokenForUser(User user, String passwordToken);

    User findUserByPasswordToken(String passwordResetToken);

    void changePassword(User user, String newPassword);

    boolean oldPasswordIsValid(User user, String oldPassword);
}
