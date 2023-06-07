package com.carspottingapp.model.response;

import com.carspottingapp.model.CarBrand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarBrandResponse {
    private Long id;
    private String brand;

    public CarBrandResponse(CarBrand brand) {
        this.id = brand.getCarBrandId();
        this.brand = brand.getCarBrandName();
    }
}
