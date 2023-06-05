package com.carspottingapp.spottedCarControllers;

import com.carspottingapp.exceptions.InvalidIdException;
import com.carspottingapp.spottedCarModels.responses.CarManufacturerResponse;
import com.carspottingapp.spottedCarServices.CarManufacturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/carmanufacturer")
public class CarManufacturerConroller {
    private final CarManufacturerService carManufacturerService;

    public CarManufacturerConroller(CarManufacturerService carManufacturerService) {
        this.carManufacturerService = carManufacturerService;
    }

    @GetMapping
    public ResponseEntity<List<CarManufacturerResponse>> getCarManufacturerById() {
        try {
            return new ResponseEntity<>(carManufacturerService.getCarManufacturer(), HttpStatus.OK) ;
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Manufacturer not found", e);
        }
    }
}
