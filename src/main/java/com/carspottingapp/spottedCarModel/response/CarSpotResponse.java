package com.carspottingapp.spottedCarModel.response;

import com.carspottingapp.spottedCarModel.CarSpot;
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
    private String brand;
    private String model;
    private String description;
    private String photoLink;
    private LocalDateTime date;

    public CarSpotResponse(CarSpot carSpot){
        this.id = carSpot.getCarSpotId();
        this.title = carSpot.getCarSpotTitle();
        this.brand = carSpot.getCarModel().getCarBrand().getCarBrand();
        this.model = carSpot.getCarModel().getCarModel();
        this.description = carSpot.getDescription();
        this.photoLink = carSpot.getPictureUrl();
        this.date = carSpot.getSpotDate();
    }
}
