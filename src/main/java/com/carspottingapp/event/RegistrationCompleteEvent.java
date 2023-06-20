package com.carspottingapp.event;

import com.carspottingapp.model.CarSpotUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private CarSpotUser carSpotUser;
    private String applicationUrl;

    public RegistrationCompleteEvent(CarSpotUser carSpotUser, String applicationUrl) {
        super(carSpotUser);
        this.carSpotUser = carSpotUser;
        this.applicationUrl = applicationUrl;
    }
}
