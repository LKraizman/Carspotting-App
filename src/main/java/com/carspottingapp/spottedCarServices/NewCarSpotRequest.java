package com.carspottingapp.spottedCarServices;

import java.time.LocalDateTime;

public class NewCarSpotRequest {
    public String carSpotTitle;
    public Long carModelId;
    public LocalDateTime spotDate;

    public NewCarSpotRequest(String title, Long carModelId) {
        this.carSpotTitle = title;
        this.carModelId = carModelId;
    }
}
