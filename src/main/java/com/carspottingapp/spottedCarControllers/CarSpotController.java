package com.carspottingapp.spottedCarControllers;

import com.carspottingapp.spottedCar.CarSpot;
import com.carspottingapp.spottedCarServices.NewSpottedCarRequest;
import com.carspottingapp.spottedCarServices.SpottedCarRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarSpotController {
    @PostMapping
    public void addSpottedcar(@RequestBody NewSpottedCarRequest request) {
        CarSpot carSpot = new CarSpot();
        carSpot.setTitle(request.carSpotTitle);
        carSpot.setCarManufacture(request.carManufacture);
        carSpot.setCarModel(request.carModel);
    }

    public final SpottedCarRepository spottedCarRepository;

    public CarSpotController(SpottedCarRepository spottedCarRepository) {
        this.spottedCarRepository = spottedCarRepository;
    }

    @GetMapping
    public List<CarSpot> getSpottedCars() {
        return spottedCarRepository.findAll();
    }
}
