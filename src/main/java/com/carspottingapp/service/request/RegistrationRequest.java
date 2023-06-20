package com.carspottingapp.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public class RegistrationRequest{

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;

}
