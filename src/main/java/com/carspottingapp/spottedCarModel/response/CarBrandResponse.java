package com.carspottingapp.spottedCarModel.response;

import com.carspottingapp.spottedCarModel.CarBrand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarBrandResponse {
    private Long id;
    private String manufacturer;

    public CarBrandResponse(CarBrand brand) {
        this.id = brand.getCarBrandId();
        this.manufacturer = brand.getCarBrand();
    }
}
