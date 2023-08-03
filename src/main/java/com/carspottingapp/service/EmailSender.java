package com.carspottingapp.service;

import com.carspottingapp.model.User;
import com.carspottingapp.utils.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String url, User user, String title) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("spotitapplication@gmail.com", EmailTemplate.EMAIL_TITLE);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(title);
        messageHelper.setText(EmailTemplate.emailContentConfigurator(user, url, title), true);
        javaMailSender.send(message);
    }
}
