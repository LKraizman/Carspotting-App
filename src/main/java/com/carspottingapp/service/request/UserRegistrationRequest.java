package com.carspottingapp.service.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;

}
