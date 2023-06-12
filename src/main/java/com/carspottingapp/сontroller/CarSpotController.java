package com.carspottingapp.сontroller;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.exception.InvalidLengthException;
import com.carspottingapp.model.response.CarSpotResponse;
import com.carspottingapp.service.CarSpotService;
import com.carspottingapp.service.NewCarSpotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/car-spots")
public class CarSpotController {
    private final CarSpotService carSpotService;

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

    @GetMapping("/{carSpotId}")
    public ResponseEntity<CarSpotResponse> getCarSpotById(@PathVariable Long carSpotId) {
        try {
            return new ResponseEntity<>(carSpotService.getCarSpotById(carSpotId), HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car Spot not found", e);
        }
    }

    @PutMapping("/{carSpotId}")
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

    @DeleteMapping("/{carSpotId}")
    public void deleteCarSpot(@PathVariable("carSpotId") Long id) {
        try {
            carSpotService.deleteCarSpot(id);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car Spot not found", e);
        }
    }
}
