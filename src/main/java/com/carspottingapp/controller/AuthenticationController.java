package com.carspottingapp.controller;

import com.carspottingapp.event.RegistrationCompleteEvent;
import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.model.User;
import com.carspottingapp.model.response.UserResponse;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.service.UserService;
import com.carspottingapp.service.TokenVerificationService;
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
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;

@Tag(name = "User registration and verification", description = "Users management API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class AuthenticationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final HttpServletRequest servletRequest;
    private final TokenVerificationService tokenVerificationService;

    @Operation(
            summary = "User registration",
            description = "Registration request for new users"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json")})})
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody UserDataRequest registrationRequest,
            final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        try {
            return new ResponseEntity<>(userService.getUserById(user.getId()), HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @Operation(
            summary = "User account verification",
            description = "Verification by link"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        String newVerificationUrl = String.format(
                "%s/register/resend-verification-token?token=%s",
                applicationUrl(servletRequest),
                token);
        if (tokenVerificationService.isTokenExist(token) == null) {
            return "This account is already verified. Try login";
        }
        String verificationResult = tokenVerificationService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return "You account successfully verified. Now you can login to your account.";
        }
        return String.format("Invalid verification link. " +
                "Please regenerate the verification response: " +
                "<a href=\"%s\">Get a new verification link. </a>", newVerificationUrl);
    }

    @Operation(
            summary = "New verification link",
            description = "Send a new verification link, if the oldest is expired"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(
            @RequestParam("token") String oldToken,
            final HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationToken = tokenVerificationService.generateNewVerificationToken(oldToken);
        User theUser = verificationToken.getUser();
        userService.resendVerificationTokenEmail(theUser, applicationUrl(request), verificationToken);
        return "A new verification link has been sent to your email";
    }

    public String applicationUrl(HttpServletRequest request) {
        URIBuilder requestUrlBuilder = new URIBuilder()
                .setScheme("http")
                .setHost(request.getServerName())
                .setPort(request.getServerPort())
                .setPath(request.getContextPath());
        return requestUrlBuilder.toString();
    }
}
