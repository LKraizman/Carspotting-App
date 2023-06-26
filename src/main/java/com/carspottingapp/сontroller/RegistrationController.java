package com.carspottingapp.сontroller;

import com.carspottingapp.event.RegistrationCompleteEvent;
import com.carspottingapp.event.listener.RegistrationCompleteEventListener;
import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.model.User;
import com.carspottingapp.model.response.UserResponse;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.service.UserService;
import com.carspottingapp.service.TokenVerificationService;
import com.carspottingapp.service.request.PasswordRequest;
import com.carspottingapp.service.request.UserDataRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final RegistrationCompleteEventListener registrationCompleteEventListener;
    private final HttpServletRequest servletRequest;
    private final TokenVerificationService tokenVerificationService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody UserDataRequest registrationRequest,
            final HttpServletRequest request){
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        try {
            return new ResponseEntity<>(userService.getUserById(user.getId()), HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token")String token){
        String newVerificationUrl = applicationUrl(servletRequest)+"/register/resend-verification-token?token="+token;
        if(tokenVerificationService.isTokenExist(token) == null){
            return "This account is already verified. Try login";
        }
        String verificationResult = tokenVerificationService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("valid")){
            return "You account successfully verified. Now you can login to your account.";
        }
        return "Invalid verification link. Please regenerate the verification response: " +
                "<a href=\""+newVerificationUrl+"\"> Get a new verification link. </a>";
    }

    @GetMapping("/resend-berification-token")
    public String resendVerificationToken(
            @RequestParam("token") String oldToken,
            final HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationToken = tokenVerificationService.generateNewVerificationToken(oldToken);
        User theUser = verificationToken.getUser();
        userService.resendVerificationTokenEmail(theUser, applicationUrl(request), verificationToken);
        return "A new verification link has been sent to your email";
    }

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
