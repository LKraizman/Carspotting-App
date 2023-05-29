package com.carspottingapp.spottedCarServices;

import com.carspottingapp.exceptions.InvalidIdException;
import com.carspottingapp.exceptions.InvalidTitleLengthException;
import com.carspottingapp.repositories.CarSpotRepository;
import com.carspottingapp.spottedCarModels.CarSpot;
import com.carspottingapp.spottedCarModels.responses.CarSpotResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarSpotService {
    public final CarSpotRepository carSpotRepository;

    public CarSpotService(CarSpotRepository spottedCarRepository) {
        this.carSpotRepository = spottedCarRepository;
    }

    public List<CarSpot> getCarSpots() {
        return carSpotRepository.findAll();
    }

    public CarSpotResponse getCarSpotById(Long id) {
        return carSpotRepository.findById(id).map(carSpot ->
                new CarSpotResponse(
                        carSpot.getCarSpotId(),
                        carSpot.getCarSpotTitle(),
                        carSpot.getCarModel().getCarManufacturer().getCarManufacturer(),
                        carSpot.getCarModel().getCarModel(),
                        carSpot.getSpotDate())).
                orElseThrow(()
                -> new InvalidIdException("Car Spot not found"));
    }

    public CarSpot addCarSpot(NewCarSpotRequest request) throws InvalidTitleLengthException {
        CarSpot newCarSpot = new CarSpot(request.carSpotTitle, request.carModelId, LocalDateTime.now());
        if (request.carSpotTitle.length() > 30) {
            throw new InvalidTitleLengthException("Incorrect title length. Must be less then 30 characters");
        }
        return carSpotRepository.save(newCarSpot);
    }

    public CarSpot editCarSpot(Long id, NewCarSpotRequest request) throws InvalidTitleLengthException {
        CarSpot updateCarSpot = carSpotRepository.findById(id).orElseThrow(()
                -> new InvalidIdException("Car Spot not found. Try another ID"));
        if (request.carSpotTitle.length() > 30) {
            throw new InvalidTitleLengthException("Incorrect title length. Must be less then 30 characters");
        }
        updateCarSpot.setCarSpotTitle(request.carSpotTitle);
        updateCarSpot.setCarModelId(request.carModelId);
        updateCarSpot.setSpotDate(request.spotDate);
        return carSpotRepository.save(updateCarSpot);
    }

    public void deleteCarSpot(Long id) {
        carSpotRepository.findById(id).orElseThrow(()
                -> new InvalidIdException("Car Spot not found. Try another ID"));
        carSpotRepository.deleteById(id);
    }
}
