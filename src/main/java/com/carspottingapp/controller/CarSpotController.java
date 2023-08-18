package com.carspottingapp.controller;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.exception.InvalidLengthException;
import com.carspottingapp.model.response.CarSpotResponse;
import com.carspottingapp.service.CarSpotService;
import com.carspottingapp.service.request.NewCarSpotRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Spotted cars", description = "Spotted cars management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/car-spots")
public class CarSpotController {
    private final CarSpotService carSpotService;

    @Operation(
            summary = "Add new car's spot",
            description = "Create new car's spot with some information about it"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CarSpotResponse.class),
                            mediaType = "application/json")})})
    @PostMapping
    public ResponseEntity<CarSpotResponse> addCarSpot(@RequestBody NewCarSpotRequest request) {
        try {
            return new ResponseEntity<>(carSpotService.addCarSpot(request), HttpStatus.OK);
        } catch (InvalidLengthException invalidLengthException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, invalidLengthException.getMessage(), invalidLengthException);
        }
    }

    @Operation(
            summary = "Retrieve all car's spots",
            description = "Get information about all existing user's car spots"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CarSpotResponse.class),
                            mediaType = "application/json")})})
    @GetMapping
    public ResponseEntity<List<CarSpotResponse>> getCarSpots() {
        return new ResponseEntity<>(carSpotService.getCarSpots(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve specific car's spot by ID",
            description = "Get information about car's spot"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CarSpotResponse.class),
                            mediaType = "application/json")})})
    @GetMapping("/{carSpotId}")
    public ResponseEntity<CarSpotResponse> getCarSpotById(@PathVariable Long carSpotId) {
        try {
            return new ResponseEntity<>(carSpotService.getCarSpotById(carSpotId), HttpStatus.OK);
        } catch (InvalidIdException invalidIdException) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, invalidIdException.getMessage(), invalidIdException);
        }
    }

    @Operation(
            summary = "Change specific car's spot by ID",
            description = "Edit information about car's spot"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CarSpotResponse.class),
                            mediaType = "application/json")})})
    @PutMapping("/{carSpotId}")
    public ResponseEntity<CarSpotResponse> editCarSpot(@PathVariable("carSpotId") Long id,
                                                       @RequestBody NewCarSpotRequest request) {
        try {
            return new ResponseEntity<>(carSpotService.editCarSpot(id, request), HttpStatus.OK);
        } catch (InvalidLengthException invalidLengthException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, invalidLengthException.getMessage(), invalidLengthException);
        } catch (InvalidIdException invalidIdException) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, invalidIdException.getMessage(), invalidIdException);
        }
    }

    @Operation(
            summary = "Delete specific car's spot by ID",
            description = "Delete car's spot"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CarSpotResponse.class),
                            mediaType = "application/json")})})
    @DeleteMapping("/{carSpotId}")
    public void deleteCarSpot(@PathVariable("carSpotId") Long id) {
        try {
            carSpotService.deleteCarSpot(id);
        } catch (InvalidIdException invalidIdException) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, invalidIdException.getMessage(), invalidIdException);
        }
    }
}
