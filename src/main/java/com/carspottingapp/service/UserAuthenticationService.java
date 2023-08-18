package com.carspottingapp.service;

import com.carspottingapp.client.GitHubClient;
import com.carspottingapp.exception.*;
import com.carspottingapp.model.OAuthGoogleInfo;
import com.carspottingapp.model.OAuthGitHubInfo;
import com.carspottingapp.model.User;
import com.carspottingapp.model.UserRole;
import com.carspottingapp.model.response.AuthenticationResponse;
import com.carspottingapp.model.response.authModel.GitHubUserEmailResponse;
import com.carspottingapp.model.token.AccessToken;
import com.carspottingapp.model.token.TokenType;
import com.carspottingapp.repository.AccessTokenRepository;
import com.carspottingapp.repository.UserRepository;
import com.carspottingapp.service.request.PasswordRequest;
import com.carspottingapp.service.request.UserAuthenticationRequest;
import com.carspottingapp.service.request.UserRegistrationRequest;
import com.carspottingapp.utils.EmailTemplate;
import com.carspottingapp.utils.UrlBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final UrlBuilder urlBuilder;
    private final GitHubClient gitHubClient;

    private static final String EMAIL_REGEX =
            "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,30}$";

    public AuthenticationResponse register(
            UserRegistrationRequest registrationRequest,
            final HttpServletRequest request) {
        Optional<User> carSpotUser = userRepository.findByEmail(registrationRequest.getEmail());
        if (carSpotUser.isPresent()) {
            throw new UserAlreadyExistException("User with email" + registrationRequest.getEmail() + " already exist");
        }
        if (validateUserPassword(registrationRequest.getPassword())) {
            throw new InvalidPasswordException("Incorrect password input: Password must meet certain criteria.");
        }
        var savedUser = userRepository.save(User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .username(registrationRequest.getUserName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .userRole(UserRole.USER)
                .isEnabled(false)
                .build());
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        saveUserToken(savedUser, jwtToken);
        try {
            emailSender.sendEmail(
                    urlBuilder.getVerificationUrl(
                            request,
                            UrlBuilder.VERIFY_ADDRESS,
                            jwtToken),
                    savedUser,
                    EmailTemplate.ACCOUNT_VERIFICATION_TITLE);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(UserAuthenticationRequest authenticationRequest) {
        User authenticatingUser = userRepository.findByEmail(
                authenticationRequest.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), authenticatingUser.getPassword())) {
            throw new InvalidPasswordException("Invalid login data");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticatingUser.getUsername(),
                        authenticationRequest.getPassword()));
        var jwtToken = jwtService.generateToken(authenticatingUser);
        var refreshToken = jwtService.generateRefreshToken(authenticatingUser);
        revokeAllUserTokens(authenticatingUser);
        saveUserToken(authenticatingUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AccessToken isTokenExist(String token) {
        Optional<AccessToken> optionalVerifiedToken = tokenRepository.findByToken(token);
        AccessToken verifiedToken = optionalVerifiedToken.orElseThrow(InvalidTokenException::new);
        if (verifiedToken.getUser().getIsEnabled()) {
            return null;
        }
        return verifiedToken;
    }

    public String userEmailVerification(String verificationToken) {
        if (isTokenExist(verificationToken) == null) {
            return "This account is already verified. Try login";
        }

        String verificationResult = validateUser(verificationToken);

        if (verificationResult.equalsIgnoreCase("valid")) {
            return "You account successfully verified. Now you can login.";
        }
        return String.format("Invalid verification link. " +
                "Please regenerate the verification response: " +
                "<a href=\"%s\">Get a new verification link. </a>", urlBuilder.getVerificationUrl(
                servletRequest,
                UrlBuilder.VERIFY_ADDRESS,
                verificationToken));
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

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        refreshToken = parseJwtTokenFromHeader(authHeader);
        username = jwtService.extractUserName(refreshToken);
        if (username != null) {
            var user = userRepository.findByUsername(username)
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

    public String sendResetPasswordEmail(
            PasswordRequest passwordResetRequest,
            HttpServletRequest request)
            throws MessagingException, IOException {
        Optional<User> user = userRepository.findByEmail(passwordResetRequest.getEmail());
        if (user.isPresent()) {
            var passwordResetToken = jwtService.generateToken(user.get());
            revokeAllUserTokens(user.get());
            saveUserToken(user.get(), passwordResetToken);
            emailSender.sendEmail(
                    urlBuilder.getVerificationUrl(
                            request,
                            UrlBuilder.RESET_ADDRESS,
                            passwordResetToken),
                    user.get(),
                    EmailTemplate.PASSWORD_RESET_TITLE);
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
                        tokenRepository.findByToken(passwordResetToken)
                                .orElseThrow(InvalidTokenException::new)
                                .getUser()
                                .getId())
                .orElseThrow(InvalidIdException::new);
        if (validateUserPassword(passwordRequestUtil.getNewPassword())) {
            throw new InvalidPasswordException("Incorrect password input");
        }
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

    private String parseJwtTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
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

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private boolean validateUserPassword(String password) {
        Pattern userPasswordPattern = Pattern.compile(EMAIL_REGEX);
        Matcher userPasswordMatcher = userPasswordPattern.matcher(password);
        return userPasswordMatcher.matches();
    }

    public URI gitHubLinkBuilder() {
        return gitHubClient.getOauthUri();
    }

    public AuthenticationResponse verifyGitHubUser(String code, String state) {
        Mono<OAuthGitHubInfo> oauthHitHubUserInfo = gitHubClient.getOauthUserToken(state, code);

        Flux<GitHubUserEmailResponse> gitHubUserEmailResponseMono = gitHubClient.getGitHubUserEmails(
                Objects.requireNonNull(oauthHitHubUserInfo.block()).getAccess_token());

        String gitHubUserPrimaryEmail =
                gitHubUserEmailResponseMono
                        .filter(GitHubUserEmailResponse::isPrimary)
                        .map(GitHubUserEmailResponse::getEmail)
                        .blockFirst();

        var gitHubUser = userRepository.save(User.of(gitHubUserPrimaryEmail));
        var jwtToken = jwtService.generateToken(gitHubUser);
        var refreshToken = jwtService.generateRefreshToken(gitHubUser);
        saveUserToken(gitHubUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
