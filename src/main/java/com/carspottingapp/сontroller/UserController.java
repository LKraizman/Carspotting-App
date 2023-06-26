package com.carspottingapp.сontroller;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.model.User;
import com.carspottingapp.model.response.UserResponse;
import com.carspottingapp.service.UserService;
import com.carspottingapp.service.request.UserDataRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserInformation(@PathVariable("userId") Long id,
                                                              @RequestBody UserDataRequest updateUserDataRequest) {
        try {
            return new ResponseEntity<>(userService.changeUserInformation(id, updateUserDataRequest), HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found", e);
        }
    }
}
