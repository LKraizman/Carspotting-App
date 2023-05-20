package com.carspottingapp.spottedCarServices;

import java.time.LocalDateTime;

public class NewCarSpotRequest {
    public String carSpotTitle;
    public String carManufacture;
    public String carModel;
    public LocalDateTime spotDate;

    public NewCarSpotRequest(String title, String carManufacture, String carModel) {
        this.carSpotTitle = title;
        this.carManufacture = carManufacture;
        this.carModel = carModel;
        this.spotDate = LocalDateTime.now();
    }

}
