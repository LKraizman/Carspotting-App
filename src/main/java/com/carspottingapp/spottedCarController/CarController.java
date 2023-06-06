package com.carspottingapp.spottedCarController;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.spottedCarModel.response.CarBrandResponse;
import com.carspottingapp.spottedCarModel.response.CarModelResponse;
import com.carspottingapp.spottedCarService.CarBrandService;
import com.carspottingapp.spottedCarService.CarModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cars")
public class CarController {
    private final CarModelService carModelService;
    private final CarBrandService carBrandService;

    @GetMapping("/all-brands")
    public ResponseEntity<List<CarBrandResponse>> getCarBrandById() {
        try {
            return new ResponseEntity<>(carBrandService.getCarBrands(), HttpStatus.OK) ;
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Brand not found", e);
        }
    }
    @GetMapping("/all-brands/{carBrandId}/models")
    public ResponseEntity<List<CarModelResponse>> getCarModelsById(@PathVariable Long carBrandId) {
        try {
            List<CarModelResponse> models = carModelService.getModelsByBrand(carBrandId);
            return new ResponseEntity<>(models, HttpStatus.OK) ;
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Brand not found", e);
        }
    }
}
