package com.carspottingapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.stereotype.Service;

@Getter
@Service
public class UrlBuilder {

    public static final String VERIFY_ADDRESS = "verifyEmail";
    public static final String RESET_ADDRESS = "reset-password";

    public String getVerificationUrl (HttpServletRequest request, String address, String jwtToken){
        return String.format("%s/api/auth/%s?token=%s", applicationUrl(request), address, jwtToken);
    }

    private String applicationUrl(HttpServletRequest request) {
        URIBuilder requestUrlBuilder = new URIBuilder()
                .setScheme("http")
                .setHost(request.getServerName())
                .setPort(request.getServerPort())
                .setPath(request.getContextPath());
        return requestUrlBuilder.toString();
    }
}
