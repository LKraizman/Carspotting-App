package com.carspottingapp.controller;

import com.carspottingapp.event.listener.RegistrationCompleteEventListener;
import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.model.User;
import com.carspottingapp.model.response.UserResponse;
import com.carspottingapp.service.TokenVerificationService;
import com.carspottingapp.service.UserService;
import com.carspottingapp.service.request.PasswordRequest;
import com.carspottingapp.service.request.UserDataRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Users", description = "Service users information API")
@Slf4j
@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenVerificationService tokenVerificationService;
    private final RegistrationCompleteEventListener registrationCompleteEventListener;


    @Operation(
            summary = "Retrieve all users",
            description = "Get information about all users, existing in service database"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json")})})
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }


    @Operation(
            summary = "Retrieve specified user by ID",
            description = "Get information about user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json")})})
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserInformation(@PathVariable("userId") Long id,
                                                              @RequestBody UserDataRequest updateUserDataRequest) {
        try {
            return new ResponseEntity<>(userService.changeUserInformation(id, updateUserDataRequest), HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @Operation(
            summary = "Password reset request",
            description = "Get a password reset URL if the user forgets his password"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(
            @RequestBody PasswordRequest passwordRequestUtil,
            final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = userService.findByEmail(passwordRequestUtil.getEmail());
        String passwordResetUrl = "";
        if(user.isPresent()){
            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), applicationUrl(request), passwordResetToken);
        }
        return passwordResetUrl;
    }

    @Operation(
            summary = "Password reset action",
            description = "Change the user's password in the service database"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordRequest passwordRequestUtil,
                                @RequestParam("token") String passwordResetToken){
        String tokenValidationResult = tokenVerificationService.validatePasswordResetToken(passwordResetToken);
        if(!tokenValidationResult.equalsIgnoreCase("valid")){
            return "Invalid password reset token";
        }
        User user = userService.findUserByPasswordToken(passwordResetToken);
        if(user != null){
            userService.changePassword(user, passwordRequestUtil.getNewPassword());
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }
    private String passwordResetEmailLink(
            User user,
            String applicationUrl,
            String passwordResetToken)
            throws MessagingException, UnsupportedEncodingException {
        String verificationUrl = applicationUrl+"/api/register/reset-password?token="+passwordResetToken;
        registrationCompleteEventListener.sendPasswordResetVerificationEmail(verificationUrl);
        log.info("Click the link to reset your password : {}", verificationUrl);
        return verificationUrl;
    }

    @Operation(
            summary = "Password changing",
            description = "Change the user's password at will"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/change-password")
    public String changePassword(@RequestBody PasswordRequest passwordRequestUtil){
        User user = userService.findByEmail(passwordRequestUtil.getEmail()).get();
        if(!userService.oldPasswordIsValid(user, passwordRequestUtil.getOldPassword())){
            return "Incorrect user password";
        }
        userService.changePassword(user, passwordRequestUtil.getNewPassword());
        return "Password changed successfully";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
