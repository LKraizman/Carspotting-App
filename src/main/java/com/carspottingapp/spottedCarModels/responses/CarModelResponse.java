package com.carspottingapp.spottedCarModels.responses;

import com.carspottingapp.spottedCarModels.CarManufacturer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarModelResponse {
    private Long id;
    private String model;

}
