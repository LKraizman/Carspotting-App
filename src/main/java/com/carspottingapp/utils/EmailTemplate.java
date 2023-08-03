package com.carspottingapp.utils;

import com.carspottingapp.model.User;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Getter
@Service
public class EmailTemplate {
    public static final String ACCOUNT_VERIFICATION_TITLE = "Email Verification";
    public static final String PASSWORD_RESET_TITLE = "Password Reset Request Verification";
    public static final String EMAIL_TITLE = "SpotIt";

    public static String emailContentConfigurator(User user, String url, String title) {
        return switch (title) {
            case "Email Verification" -> String.format(
                    "<p>Hi, %s, </p>"
                            + "<p>Thank you for registration on SpotIt! Please, follow the link below to complete your registration.</p>"
                            + "<p><a href=\"%s\">Verify your email to activate your account</a></p>"
                            + "<p> Thank you, <br> SpotIt Registration Portal Service", user.getFirstName(), url);
            case "Password Reset Request Verification" -> String.format(
                    "<p>Hi, %s, </p>"
                            + "<p><b>You recently requested to reset your password.</b></p>, "
                            + "<p>Please, follow the link below to complete this action:</p>"
                            + "<p><a href=\"%s\">Reset password</a></p>"
                            + "<p> Thank you, <br> SpotIt Service", user.getFirstName(), url);
            default -> "Incorrect request";
        };
    }
}
