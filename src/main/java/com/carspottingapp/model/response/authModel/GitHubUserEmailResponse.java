package com.carspottingapp.model.response.authModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubUserEmailResponse {
    private String email;
    private boolean primary;

    public GitHubUserEmailResponse(){

    }
}
