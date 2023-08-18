package com.carspottingapp.model;

import com.carspottingapp.model.response.UserResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserActions {

    List<UserResponse> getUsers();

    UserResponse findById(Long id);

    void changePassword(User user, String newPassword);

    boolean IsOldPasswordValid(User user, String oldPassword);
}
