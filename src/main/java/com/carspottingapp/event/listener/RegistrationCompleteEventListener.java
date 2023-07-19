package com.carspottingapp.event.listener;

import com.carspottingapp.event.RegistrationCompleteEvent;
import com.carspottingapp.model.User;
import com.carspottingapp.service.EmailSender;
import com.carspottingapp.service.TokenVerificationService;
import com.carspottingapp.utils.EmailContent;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final EmailSender emailSender;
    private final TokenVerificationService tokenVerificationService;
    private final EmailContent emailContent;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Get the newly registered user
        User user = event.getUser();
        // Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        // Save the verification token for the user
        tokenVerificationService.saveUserVerificationToken(user, verificationToken);
        // Build the verification url to be sent to the user
        String verificationUrl = String.format("%s/api/register/verifyEmail?token=%s", event.getApplicationUrl(), verificationToken);
        // Sending the email
        try {
            emailSender.sendEmail(verificationUrl, user, emailContent.getACCOUNT_VERIFICATION_TITLE());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to complete your registration : {}", verificationUrl);
    }
}
