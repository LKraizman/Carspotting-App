package com.carspottingapp.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDataRequest {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;

}
