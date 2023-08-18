package com.carspottingapp.service.properties;

import lombok.Data;

@Data
public class Client {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
}
