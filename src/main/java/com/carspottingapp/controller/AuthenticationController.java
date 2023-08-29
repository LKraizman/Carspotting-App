package com.carspottingapp.controller;

import com.carspottingapp.exception.InvalidPasswordException;
import com.carspottingapp.exception.UserAlreadyExistException;
import com.carspottingapp.exception.UserNotFoundException;
import com.carspottingapp.model.response.AuthenticationResponse;
import com.carspottingapp.service.request.PasswordRequest;
import com.carspottingapp.service.request.UserAuthenticationRequest;
import com.carspottingapp.service.UserAuthenticationService;
import com.carspottingapp.service.request.UserRegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Tag(name = "User registration, verification and authentication", description = "Users management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserAuthenticationService userAuthenticationService;

    @RequestMapping(value = "/githubAuth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getGitHubLink() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(userAuthenticationService.gitHubLinkBuilder())
                .build();
    }

    @RequestMapping(value = "/verifyGitHubUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> verifyGitHubUser(
            @RequestParam("state") String state,
            @RequestParam("code") String code) {
        return ResponseEntity.ok(userAuthenticationService.verifyGitHubUser(state, code));
    }

    @RequestMapping(value = "/googleAuth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getGoogleAuthLink() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(userAuthenticationService.googleAuthLinkBuilder())
                .build();
    }

    @RequestMapping(value = "/verifyGoogleUser")
    public ResponseEntity<AuthenticationResponse> verifyGoogleUser(
            @RequestParam("code") String code) {
        return ResponseEntity.ok(userAuthenticationService.verifyGoogleUser(code));
    }

    @Operation(
            summary = "User registration",
            description = "Registration request for new users"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = AuthenticationResponse.class),
                            mediaType = "application/json")})})
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody UserRegistrationRequest registrationRequest,
            final HttpServletRequest httpServletRequest) {
        try {
            return ResponseEntity.ok(userAuthenticationService.register(registrationRequest, httpServletRequest));
        } catch (UserAlreadyExistException userAlreadyExistException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, userAlreadyExistException.getMessage(), userAlreadyExistException);
        } catch (InvalidPasswordException invalidPasswordException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, invalidPasswordException.getMessage(), invalidPasswordException);
        }

    }

    @Operation(
            summary = "User account verification",
            description = "Verification by link"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        return userAuthenticationService.userEmailVerification(token);
    }

    @Operation(
            summary = "User login",
            description = "Login by authentication"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody UserAuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(userAuthenticationService.authenticate(authenticationRequest));
        } catch (UserNotFoundException userNotFoundException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, userNotFoundException.getMessage(), userNotFoundException);
        } catch (InvalidPasswordException invalidPasswordException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, invalidPasswordException.getMessage(), invalidPasswordException);
        }
    }

    @Operation(
            summary = "Refresh access tokens",
            description = "Refresh access tokens for any reasons"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userAuthenticationService.refreshToken(request, response);
    }

    @Operation(
            summary = "Password reset request",
            description = "Get a password reset URL if the user forgets password"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(
            @RequestBody PasswordRequest passwordResetRequest,
            final HttpServletRequest request) throws MessagingException, IOException {
        return userAuthenticationService.sendResetPasswordEmail(passwordResetRequest, request);
    }

    @Operation(
            summary = "Password reset action",
            description = "Change the user's password in the service database"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordRequest passwordRequestUtil,
                                @RequestParam("token") String passwordResetToken) {
        try {
            return userAuthenticationService.setNewUserPassword(passwordRequestUtil, passwordResetToken);
        } catch (InvalidPasswordException invalidPasswordException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, invalidPasswordException.getMessage(), invalidPasswordException);
        }
    }
}
