package com.carspottingapp.service;

import com.carspottingapp.exception.UserAlreadyExistException;
import com.carspottingapp.model.User;
import com.carspottingapp.model.UserRole;
import com.carspottingapp.model.response.AuthenticationResponse;
import com.carspottingapp.model.token.AccessToken;
import com.carspottingapp.model.token.TokenType;
import com.carspottingapp.repository.AccessTokenRepository;
import com.carspottingapp.repository.UserRepository;
import com.carspottingapp.service.request.PasswordRequest;
import com.carspottingapp.service.request.UserAuthenticationRequest;
import com.carspottingapp.service.request.UserRegistrationRequest;
import com.carspottingapp.utils.EmailTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationService {
    private final UserRepository userRepository;
    private final AccessTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final HttpServletRequest servletRequest;

    public AuthenticationResponse register(
            UserRegistrationRequest registrationRequest,
            final HttpServletRequest request) {
        Optional<User> carSpotUser = userRepository.findByEmail(registrationRequest.getEmail());
        if (carSpotUser.isPresent()) {
            throw new UserAlreadyExistException("User with email" + registrationRequest.getEmail() + " already exist");
        }
        var user = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .username(registrationRequest.getUserName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .userRole(UserRole.USER)
                .isEnabled(false)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        String verificationUrl = String.format("%s/api/auth/verifyEmail?token=%s", applicationUrl(request), jwtToken);
        try {
            emailSender.sendEmail(verificationUrl, savedUser, EmailTemplate.ACCOUNT_VERIFICATION_TITLE);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(UserAuthenticationRequest authenticationRequest) {
        Optional<User> carSpotUser = userRepository.findByEmail(authenticationRequest.getEmail());
        carSpotUser.ifPresent(user -> authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        authenticationRequest.getPassword()
                )
        ));
        var user = carSpotUser.orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = AccessToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public AccessToken isTokenExist(String token) {
        Optional<AccessToken> optionalVerifiedToken = tokenRepository.findByToken(token);
        AccessToken verifiedToken = optionalVerifiedToken.get();
        if (verifiedToken.getUser().getIsEnabled()) {
            return null;
        }
        return verifiedToken;
    }

    public String userVerification(String verificationToken) {
        if (isTokenExist(verificationToken) == null) {
            return "This account is already verified. Try login";
        }

        String verificationResult = validateUser(verificationToken);

        if (verificationResult.equalsIgnoreCase("valid")) {
            return "You account successfully verified. Now you can login.";
        }

        String newVerificationUrl = String.format(
                "%s/auth/verifyEmail?token=%s",
                applicationUrl(servletRequest),
                verificationToken);

        return String.format("Invalid verification link. " +
                "Please regenerate the verification response: " +
                "<a href=\"%s\">Get a new verification link. </a>", newVerificationUrl);
    }

    public String validateUser(String validationToken) {
        Optional<AccessToken> optionalToken = tokenRepository.findByToken(validationToken);
        if (optionalToken.isEmpty()) {
            return "Invalid verification token";
        }
        AccessToken token = optionalToken.get();
        if (token.expired) {
            return "Token already expired.";
        }
        User user = token.getUser();
        user.setIsEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUserName(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByUsername(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public void refreshUserToken(User user) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
    }

    public String passwordResetSender(
            PasswordRequest passwordResetRequest,
            HttpServletRequest request)
            throws MessagingException, IOException {
        Optional<User> user = userRepository.findByEmail(passwordResetRequest.getEmail());
        if (user.isPresent()) {
            var passwordResetToken = jwtService.generateToken(user.get());
            revokeAllUserTokens(user.get());
            saveUserToken(user.get(), passwordResetToken);
            String passwordResetLink = String.format(
                    "%s/api/auth/reset-password?token=%s",
                    applicationUrl(request),
                    passwordResetToken);
            emailSender.sendEmail(passwordResetLink, user.get(), EmailTemplate.PASSWORD_RESET_TITLE);
            return "Email sent to your address";
        }
        return "User not found";
    }

    public String setNewUserPassword(PasswordRequest passwordRequestUtil, String passwordResetToken) {
        String tokenValidationResult = validateResetToken(passwordResetToken);
        if (!tokenValidationResult.equalsIgnoreCase("valid")) {
            return "Invalid password reset token";
        }
        User user = userRepository.findById(
                        tokenRepository
                                .findByToken(passwordResetToken)
                                .get()
                                .getUser()
                                .getId())
                        .get();
        user.setPassword(passwordEncoder.encode(passwordRequestUtil.getNewPassword()));
        userRepository.save(user);
        return "Password has been reset successfully";
    }

    public String validateResetToken(String passwordResetToken) {
        Optional<AccessToken> optionalToken = tokenRepository.findByToken(passwordResetToken);
        if (optionalToken.isEmpty()) {
            return "Invalid verification token";
        }
        AccessToken token = optionalToken.get();
        if (token.expired) {
            return "Token already expired.";
        }
        return "valid";
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
