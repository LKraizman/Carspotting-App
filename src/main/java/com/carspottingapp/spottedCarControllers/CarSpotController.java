package com.carspottingapp.spottedCarControllers;

import com.carspottingapp.exceptions.InvalidIdException;
import com.carspottingapp.exceptions.InvalidTitleLengthException;
import com.carspottingapp.spottedCarEntities.CarSpot;
import com.carspottingapp.spottedCarServices.CarSpotService;
import com.carspottingapp.spottedCarServices.NewCarSpotRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/carspots")
public class CarSpotController {
    private final CarSpotService carSpotService;

    public CarSpotController(CarSpotService carSpotService) {
        this.carSpotService = carSpotService;
    }

    @PostMapping
    public void addCarSpot(@RequestBody NewCarSpotRequest request) {
        try {
            carSpotService.addCarSpot(request);
        } catch (InvalidTitleLengthException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect title length", e);
        }
    }

    @GetMapping
    public List<CarSpot> getCarSpots() {
        return carSpotService.getCarSpots();
    }

    @GetMapping("/{id}")
    public CarSpot getCarSpotById(@PathVariable Long id) {
        try {
            return carSpotService.getCarSpotById(id);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car Spot not found", e);
        }
    }

    @PutMapping("{carSpotId}")
    public void editCarSpot(@PathVariable("carSpotId") Long id, @RequestBody NewCarSpotRequest request) {
        try {
            carSpotService.editCarSpot(id, request);
        } catch (InvalidTitleLengthException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect title length", e);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car Spot not found", e);
        }
    }

    @DeleteMapping("{carSpotId}")
    public void deleteCarSpot(@PathVariable("carSpotId") Long id) {
        try {
            carSpotService.deleteCarSpot(id);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car Spot not found", e);
        }
    }
}
