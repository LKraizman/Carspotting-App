package com.carspottingapp.service;



import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class NewCarSpotRequest {
    public String carSpotTitle;
    public Long carModelId;
    public LocalDateTime spotDate;
    public String description;

    public String pictureUrl;

    public NewCarSpotRequest(String title, Long carModelId, String description, @NonNull String pictureUrl) {
        this.carSpotTitle = title;
        this.carModelId = carModelId;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }
}
