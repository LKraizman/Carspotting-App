package com.carspottingapp.сontroller;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.model.response.CarBrandResponse;
import com.carspottingapp.model.response.CarModelResponse;
import com.carspottingapp.service.CarBrandService;
import com.carspottingapp.service.CarModelService;
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

    @GetMapping("/brands")
    public ResponseEntity<List<CarBrandResponse>> getCarBrandById() {
        try {
            return new ResponseEntity<>(carBrandService.getCarBrands(), HttpStatus.OK) ;
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Brand not found", e);
        }
    }
    @GetMapping("/brands/{carBrandId}/models")
    public ResponseEntity<List<CarModelResponse>> getCarModelsById(@PathVariable Long carBrandId) {
        try {
            List<CarModelResponse> models = carModelService.getModelsByBrandId(carBrandId);
            return new ResponseEntity<>(models, HttpStatus.OK) ;
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Brand not found", e);
        }
    }
}
