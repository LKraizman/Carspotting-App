package com.carspottingapp.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class OAuthGoogleInfo {
    @NonNull
    private String access_token;
    @NonNull
    private String expires_in;
    @NonNull
    private String scope;
    @NonNull
    private String token_type;
    @NonNull
    private String id_token;



    public OAuthGoogleInfo() {
    }

    public OAuthGoogleInfo(@NonNull String access_token, @NonNull String expires_in, @NonNull String scope, @NonNull String token_type, @NonNull String id_token) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.token_type = token_type;
        this.id_token = id_token;
    }
}
