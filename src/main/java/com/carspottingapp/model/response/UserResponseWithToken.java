package com.carspottingapp.model.response;

import com.carspottingapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseWithToken {
    public UserResponse userResponse;
    public String accessToken;

    public UserResponseWithToken(User user, String token){
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        this.accessToken = token;
    }
}
