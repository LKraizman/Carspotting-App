package com.carspottingapp.controller;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.model.response.CarBrandResponse;
import com.carspottingapp.model.response.CarModelResponse;
import com.carspottingapp.service.CarBrandService;
import com.carspottingapp.service.CarModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Cars in database", description = "Cars brands and models API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/cars")
public class CarController {
    private final CarModelService carModelService;
    private final CarBrandService carBrandService;

    @Operation(
            summary = "Retrieve all cars brands",
            description = "Get information about all cars brands, existing in service"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CarBrandResponse.class),
                            mediaType = "application/json")})})
    @GetMapping("/brands")
    public ResponseEntity<List<CarBrandResponse>> getCarBrandById() {
        try {
            return new ResponseEntity<>(carBrandService.getCarBrands(), HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Brand not found", e);
        }
    }

    @Operation(
            summary = "Retrieve all brand's models by brand's ID",
            description = "Get information about all models by specific brand"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CarModelResponse.class),
                            mediaType = "application/json")})})
    @GetMapping("/brands/{carBrandId}/models")
    public ResponseEntity<List<CarModelResponse>> getCarModelsById(@PathVariable Long carBrandId) {
        try {
            List<CarModelResponse> models = carModelService.getModelsByBrandId(carBrandId);
            return new ResponseEntity<>(models, HttpStatus.OK);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Brand not found", e);
        }
    }
}
