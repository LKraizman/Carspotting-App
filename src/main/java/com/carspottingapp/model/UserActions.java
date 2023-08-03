package com.carspottingapp.model;

import com.carspottingapp.model.response.AuthenticationResponse;
import com.carspottingapp.model.response.UserResponse;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserActions {

    List<UserResponse> getUsers();

    Optional<User> findByEmail(String email);

    UserResponse findById(Long id);

    void changePassword(User user, String newPassword);

    boolean oldPasswordIsValid(User user, String oldPassword);
}
