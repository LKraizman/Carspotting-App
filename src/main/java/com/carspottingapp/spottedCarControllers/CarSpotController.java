package com.carspottingapp.spottedCarControllers;

import com.carspottingapp.exceptions.InvalidIdException;
import com.carspottingapp.exceptions.InvalidLengthException;
import com.carspottingapp.spottedCarModels.responses.CarSpotResponse;
import com.carspottingapp.spottedCarServices.CarSpotService;
import com.carspottingapp.spottedCarServices.NewCarSpotRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CarSpotResponse> addCarSpot(@RequestBody NewCarSpotRequest request) {
        try {
            return new ResponseEntity<>(carSpotService.addCarSpot(request), HttpStatus.OK);
        } catch (InvalidLengthException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect text length", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<CarSpotResponse>> getCarSpots() {
        return new ResponseEntity<>(carSpotService.getCarSpots(), HttpStatus.OK);
    }

    @GetMapping("{carSpotid}")
    public ResponseEntity<CarSpotResponse> getCarSpotById(@PathVariable Long carSpotid) {
        try {
            return new ResponseEntity<>(carSpotService.getCarSpotById(carSpotid), HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car Spot not found", e);
        }
    }

    @PutMapping("{carSpotId}")
    public ResponseEntity<CarSpotResponse> editCarSpot(@PathVariable("carSpotId") Long id, @RequestBody NewCarSpotRequest request) {
        try {
            return new ResponseEntity<>(carSpotService.editCarSpot(id, request), HttpStatus.OK);
        } catch (InvalidLengthException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect text length", e);
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
