package com.carspottingapp.spottedCarControllers;

import com.carspottingapp.spottedCar.SpottedCar;
import com.carspottingapp.spottedCarServices.NewSpottedCarRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddSpottedCar {
    @PostMapping
    public void addSpottedcar(@RequestBody NewSpottedCarRequest request){
        SpottedCar spottedCar = new SpottedCar();
        spottedCar.setTitle(request.title);
        spottedCar.setCarManufacture(request.carManufacture);
        spottedCar.setCarModel(request.carModel);
    }
}
