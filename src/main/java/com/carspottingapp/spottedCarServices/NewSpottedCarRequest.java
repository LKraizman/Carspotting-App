package com.carspottingapp.spottedCarServices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewSpottedCarRequest {
    public String title;
    public String carManufacture;
    public String carModel;
    public String spotDate;

    public NewSpottedCarRequest(String title, String carManufacture, String carModel) {
        this.title = title;
        this.carManufacture = carManufacture;
        this.carModel = carModel;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.spotDate = dtf.format(now);
    }

}
