package com.carspottingapp.event.listener;

import com.carspottingapp.event.RegistrationCompleteEvent;
import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.service.CarSpotUserService;
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
    private final CarSpotUserService carSpotUserService;
    private final JavaMailSender javaMailSender;
    private CarSpotUser carSpotUser;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Get the newly registered user
        carSpotUser = event.getCarSpotUser();
        // Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        // Save the verification token for the user
        carSpotUserService.saveUserVerificationToken(carSpotUser, verificationToken);
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
        String mailContent = "<p> Hi, "+ carSpotUser.getFirstName()+ ", </p>"+
                "<p>Thank you for registering on SpotIt!"+" " +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> SpotIt Registration Portal Service";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("spotitapplication@gmail.com", senderName);
        messageHelper.setTo(carSpotUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);
    }
}
