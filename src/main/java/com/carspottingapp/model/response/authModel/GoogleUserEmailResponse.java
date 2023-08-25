package com.carspottingapp.model.response.authModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUserEmailResponse {
    private String email;
    private String name;

    public GoogleUserEmailResponse(){

    }
}
