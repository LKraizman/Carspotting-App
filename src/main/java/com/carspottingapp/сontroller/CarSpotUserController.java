package com.carspottingapp.сontroller;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.exception.InvalidLengthException;
import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.model.response.CarSpotUserResponse;
import com.carspottingapp.repository.CarSpotUserRepository;
import com.carspottingapp.service.CarSpotUserService;
import com.carspottingapp.service.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class CarSpotUserController {
    private final CarSpotUserService carSpotUserService;

    @GetMapping
    public List<CarSpotUser> getUsers() {
        return carSpotUserService.getUsers();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CarSpotUserResponse> updateUserInformation(@PathVariable("userId") Long id,
                                                                     @RequestBody RegistrationRequest registrationRequest) {
        try {
            return new ResponseEntity<>(carSpotUserService.changeUserInformation(id, registrationRequest), HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found", e);
        }
    }
}
