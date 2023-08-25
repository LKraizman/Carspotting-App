package com.carspottingapp.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "google")
public class GoogleAuthProperties {
    private String apiUrl;
    private String oauthUrl;
    private String oauthPath;
    private String userApiUrl;
    private boolean includeGrantedScopes;
    private String responseType;
    private String grantType;
    private Client client;
}
