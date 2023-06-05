package com.carspottingapp.spottedCarControllers;

import com.carspottingapp.exceptions.InvalidIdException;
import com.carspottingapp.spottedCarModels.responses.CarModelResponse;
import com.carspottingapp.spottedCarServices.CarModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/model")
public class CarModelController {
    private final CarModelService carModelService;
    public CarModelController(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    @GetMapping("{carManufactureId}")
    public ResponseEntity<List<CarModelResponse>> getCarModelById(@PathVariable Long carManufactureId) {
        try {
            List<CarModelResponse> models = carModelService.getModels(carManufactureId);
            return new ResponseEntity<>(models, HttpStatus.OK) ;
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Manufacturer not found", e);
        }
    }
}
