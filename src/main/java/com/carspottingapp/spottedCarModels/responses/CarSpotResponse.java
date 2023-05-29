package com.carspottingapp.spottedCarModels.responses;

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
    private String manufacturer;
    private String model;
    private LocalDateTime date;
}
