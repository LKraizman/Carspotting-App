package com.carspottingapp.spottedCarControllers;

import com.carspottingapp.spottedCar.SpottedCar;
import com.carspottingapp.spottedCarServices.SpottedCarRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetSpottedCar {
    public final SpottedCarRepository spottedCarRepository;
    public GetSpottedCar(SpottedCarRepository spottedCarRepository){
        this.spottedCarRepository = spottedCarRepository;
    }
    @GetMapping
    public List<SpottedCar> getSpottedCar(){
        return spottedCarRepository.findAll();
    }
}
