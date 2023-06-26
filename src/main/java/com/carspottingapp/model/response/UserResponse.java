package com.carspottingapp.model.response;

import com.carspottingapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;

    public UserResponse(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}
