package com.carspottingapp.spottedCarServices;

import com.carspottingapp.exceptions.InvalidIdException;
import com.carspottingapp.exceptions.InvalidTitleLengthException;
import com.carspottingapp.spottedCarEntities.CarSpot;
import org.springframework.stereotype.Service;

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

    public CarSpot getCarSpotById(Long id) {
        return carSpotRepository.findById(id).orElseThrow(()
                -> new InvalidIdException("Car Spot not found"));
    }

    public CarSpot addCarSpot(NewCarSpotRequest request) throws InvalidTitleLengthException {
        CarSpot newCarSpot = new CarSpot(request.carSpotTitle, request.carManufacture, request.carModel, request.spotDate);
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
        updateCarSpot.setTitle(request.carSpotTitle);
        updateCarSpot.setCarManufacture(request.carManufacture);
        updateCarSpot.setCarModel(request.carModel);
        updateCarSpot.setSpotDate(request.spotDate);
        return carSpotRepository.save(updateCarSpot);
    }

    public void deleteCarSpot(Long id) {
        carSpotRepository.findById(id).orElseThrow(()
                -> new InvalidIdException("Car Spot not found. Try another ID"));
        carSpotRepository.deleteById(id);
    }
}
