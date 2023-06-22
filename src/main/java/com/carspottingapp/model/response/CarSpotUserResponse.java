package com.carspottingapp.model.response;

import com.carspottingapp.model.CarSpotUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarSpotUserResponse {
    private String username;
    private String firstName;
    private String lastName;

    public CarSpotUserResponse(CarSpotUser carSpotUser){
        this.username = carSpotUser.getUsername();
        this.firstName = carSpotUser.getFirstName();
        this.lastName = carSpotUser.getLastName();
    }
}
