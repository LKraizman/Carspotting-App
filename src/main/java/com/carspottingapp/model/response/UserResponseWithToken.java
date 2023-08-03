package com.carspottingapp.model.response;

import com.carspottingapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseWithToken {
    public Long id;
    public String firstName;
    public String lastName;
    public String username;
    public String email;
    public String accessToken;

    public UserResponseWithToken(User user, String token){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.accessToken = token;
    }
}
