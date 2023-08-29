package com.carspottingapp.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class OAuthGitHubInfo {
    @NonNull
    private String token_type;
    @NonNull
    private String scope;
    @NonNull
    private String access_token;

    public OAuthGitHubInfo() {
    }

    public OAuthGitHubInfo(@NonNull String token_type, @NonNull String scope, @NonNull String access_token) {
        this.token_type = token_type;
        this.scope = scope;
        this.access_token = access_token;
    }
}
