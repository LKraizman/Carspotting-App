package com.carspottingapp.spottedCarServices;


import java.time.LocalDateTime;

public class NewCarSpotRequest {
    public String carSpotTitle;
    public Long carModelId;
    public LocalDateTime spotDate;
    public String description;
    public String photoLink;

    public NewCarSpotRequest(String title, Long carModelId, String description, String photoLink) {
        this.carSpotTitle = title;
        this.carModelId = carModelId;
        this.description = description;
        this.photoLink = photoLink;
    }
}
