package com.carspottingapp.event.listener;

import com.carspottingapp.event.RegistrationCompleteEvent;
import com.carspottingapp.model.User;
import com.carspottingapp.service.TokenVerificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final JavaMailSender javaMailSender;
    private final TokenVerificationService tokenVerificationService;
    private User user;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Get the newly registered user
        user = event.getUser();
        // Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        // Save the verification token for the user
        tokenVerificationService.saveUserVerificationToken(user, verificationToken);
        // Build the verification url to be sent to the user
        String verificationUrl = event.getApplicationUrl()+"/api/register/verifyEmail?token="+verificationToken;
        // Sending the email
        try {
            sendVerificationEmail(verificationUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to complete your registration : {}", verificationUrl);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "SpotIt";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p>Thank you for registering on SpotIt!"+" " +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> SpotIt Registration Portal Service";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("spotitapplication@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);
    }

    public void sendPasswordResetVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request Verification";
        String senderName = "SpotIt";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p><b>You recently requested to reset your password, </b>"+" " +
                "Please, follow the link below to complete this action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> SpotIt Registration Portal Service";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("spotitapplication@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);
    }
}
