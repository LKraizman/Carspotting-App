package com.carspottingapp.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "github")
public class GitHubAuthProperties {
    private String apiUrl;
    private String apiTokenPath;
    private String apiUserEmailsPath;
    private String oauthUrl;
    private String oauthPath;
    private Client client;
}
