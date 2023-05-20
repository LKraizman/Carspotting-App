package com.carspottingapp.spottedCarServices;

import com.carspottingapp.spottedCar.CarSpot;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarSpotService {
    public final CarSpotRepository carSpotRepository;
    public CarSpotService(CarSpotRepository spottedCarRepository) {
        this.carSpotRepository = spottedCarRepository;
    }
    public List<CarSpot> getCarSpots(){
        return carSpotRepository.findAll();
    }

    public CarSpot addCarSpot(NewCarSpotRequest request){
        CarSpot newCarSpot = new CarSpot(request.carSpotTitle, request.carManufacture, request.carModel, request.spotDate);
        return carSpotRepository.save(newCarSpot);
    }

    public CarSpot editCarSpot(Long id, NewCarSpotRequest request){
        CarSpot updateCarSpot = carSpotRepository.findById(id).orElseThrow();
        updateCarSpot.setTitle(request.carSpotTitle);
        updateCarSpot.setCarManufacture(request.carManufacture);
        updateCarSpot.setCarModel(request.carModel);
        updateCarSpot.setSpotDate(request.spotDate);
        return carSpotRepository.save(updateCarSpot);
    }

    public void deleteCarSpot(Long id){
        carSpotRepository.deleteById(id);
    }

}
