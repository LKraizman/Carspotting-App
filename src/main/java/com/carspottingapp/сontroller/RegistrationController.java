package com.carspottingapp.сontroller;

import com.carspottingapp.event.RegistrationCompleteEvent;
import com.carspottingapp.event.listener.RegistrationCompleteEventListener;
import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.model.token.VerificationToken;
import com.carspottingapp.repository.VerificationTokenRepository;
import com.carspottingapp.service.CarSpotUserService;
import com.carspottingapp.service.TokenVerificationService;
import com.carspottingapp.service.request.PasswordResetRequest;
import com.carspottingapp.service.request.RegistrationRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegistrationController {
    private final CarSpotUserService carSpotUserService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RegistrationCompleteEventListener registrationCompleteEventListener;
    private final HttpServletRequest servletRequest;
    private final TokenVerificationService tokenVerificationService;
    @PostMapping
    public String registerCarSpotUser(
            @RequestBody RegistrationRequest registrationRequest,
            final HttpServletRequest request){
        CarSpotUser carSpotUser = carSpotUserService.registerUser(registrationRequest);

        publisher.publishEvent(new RegistrationCompleteEvent(carSpotUser, applicationUrl(request)));

        return "Success! Please, check your email to confirm registration";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token")String token){
        String newVerificationUrl = applicationUrl(servletRequest)+"/register/resend-berification-token?token="+token;
        VerificationToken verifiedToken = verificationTokenRepository.findByToken(token);
        if(verifiedToken.getUser().getIsEnabled()){
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
        CarSpotUser theUser = verificationToken.getUser();
        carSpotUserService.resendVerificationTokenEmail(theUser, applicationUrl(request), verificationToken);
        return "A new verification link has been sent to your email";
    }

    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(
            @RequestBody PasswordResetRequest passwordResetRequest,
            final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Optional<CarSpotUser> user = carSpotUserService.findByEmail(passwordResetRequest.getEmail());
        String passwordResetUrl = "";
        if(user.isPresent()){
            String passwordResetToken = UUID.randomUUID().toString();
            carSpotUserService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), applicationUrl(request), passwordResetToken);
        }
        return passwordResetUrl;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("token") String passwordResetToken){
        String tokenValidationResult = tokenVerificationService.validatePasswordResetToken(passwordResetToken);
        if(!tokenValidationResult.equalsIgnoreCase("valid")){
            return "Invalid password reset token";
        }
        CarSpotUser user = carSpotUserService.findUserByPasswordToken(passwordResetToken);
        if(user != null){
            carSpotUserService.resetUserPassword(user, passwordResetRequest.getNewPassword());
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }
    private String passwordResetEmailLink(
            CarSpotUser carSpotUser,
            String applicationUrl,
            String passwordResetToken)
            throws MessagingException, UnsupportedEncodingException {
        String verificationUrl = applicationUrl+"/api/register/reset-password?token="+passwordResetToken;
        registrationCompleteEventListener.sendPasswordResetVerificationEmail(verificationUrl);
        log.info("Click the link to reset your password : {}", verificationUrl);
        return verificationUrl;
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

}
