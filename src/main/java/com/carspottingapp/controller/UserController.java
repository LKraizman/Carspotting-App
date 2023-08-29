package com.carspottingapp.controller;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.model.response.UserResponse;
import com.carspottingapp.model.response.UserResponseWithToken;
import com.carspottingapp.service.UserService;
import com.carspottingapp.service.request.PasswordRequest;
import com.carspottingapp.service.request.UserRegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Tag(name = "Users", description = "Service users information API")
@Slf4j
@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Retrieve all users",
            description = "Get information about all users, existing in service database"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json")})})
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve specified user by ID",
            description = "Get user profile information for one particular user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json")})})
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserInformation(@PathVariable("userId") Long id) {
        try {
            return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
        } catch (InvalidIdException invalidIdException) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, invalidIdException.getMessage(), invalidIdException);
        }
    }

    @Operation(
            summary = "Edit user information by ID",
            description = "Update user profile information"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json")})})
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseWithToken> updateUserInformation(
            @PathVariable("userId") Long id,
            @RequestBody UserRegistrationRequest updateUserRegistrationRequest) {
        try {
            return new ResponseEntity<>(userService.changeUserInformation(
                    id,
                    updateUserRegistrationRequest),
                    HttpStatus.OK);
        } catch (InvalidIdException invalidIdException) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, invalidIdException.getMessage(), invalidIdException);
        }
    }

    @Operation(
            summary = "Password changing",
            description = "Change the user's password at will"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PutMapping("/{userId}/update-password")
    public String updatePassword(@PathVariable("userId") Long id, @RequestBody PasswordRequest passwordRequestUtil) {
        return userService.userPasswordUpdate(passwordRequestUtil, id);
    }
}
