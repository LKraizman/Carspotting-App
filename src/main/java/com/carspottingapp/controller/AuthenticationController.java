package com.carspottingapp.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "User registration, verification and authentication", description = "Users management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserAuthenticationService userAuthenticationService;

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
        return ResponseEntity.ok(userAuthenticationService.register(registrationRequest, httpServletRequest));
    }

    @Operation(
            summary = "User account verification",
            description = "Verification by link"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PostMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        return userAuthenticationService.userVerification(token);
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
        return ResponseEntity.ok(userAuthenticationService.authenticate(authenticationRequest));
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
        return userAuthenticationService.passwordResetSender(passwordResetRequest, request);
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
        return userAuthenticationService.setNewUserPassword(passwordRequestUtil, passwordResetToken);
    }
}
