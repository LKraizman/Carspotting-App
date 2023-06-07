package com.carspottingapp.model.response;

import com.carspottingapp.model.CarSpot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarSpotResponse {
    private Long id;
    private String title;
    private String description;
    private String pictureUrl;
    private LocalDateTime date;
    private CarModelResponse carModelResponse;
    private CarBrandResponse carBrandResponse;

    public CarSpotResponse(CarSpot carSpot){
        this.id = carSpot.getCarSpotId();
        this.title = carSpot.getCarSpotTitle();
        this.carBrandResponse = new CarBrandResponse(carSpot.getCarModel().getCarBrand());
        this.carModelResponse = new CarModelResponse(carSpot.getCarModel());
        this.description = carSpot.getDescription();
        this.pictureUrl = carSpot.getPictureUrl();
        this.date = carSpot.getSpotDate();
    }
}
