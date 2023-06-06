package com.carspottingapp.spottedCarModel.response;

import com.carspottingapp.spottedCarModel.CarModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarModelResponse {
    private Long id;
    private String model;

    public CarModelResponse(CarModel carModel){
        this.id = carModel.getCarModelId();
        this.model = carModel.getCarModel();
    }
}
