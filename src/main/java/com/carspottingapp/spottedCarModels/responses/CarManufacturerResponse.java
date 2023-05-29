package com.carspottingapp.spottedCarModels.responses;

import com.carspottingapp.spottedCarModels.CarManufacturer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarManufacturerResponse {
    private Long id;
    private String manufacturer;

    public CarManufacturerResponse(CarManufacturer manufacturer) {
        this.id = manufacturer.getCarManufacturerId();
        this.manufacturer = manufacturer.getCarManufacturer();
    }
}
