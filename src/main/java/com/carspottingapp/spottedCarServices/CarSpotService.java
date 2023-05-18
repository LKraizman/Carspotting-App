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

    public void addCarSpot(NewCarSpotRequest request){
        CarSpot newCarSpot = new CarSpot();
        newCarSpot.setTitle(request.carSpotTitle);
        newCarSpot.setCarManufacture(request.carManufacture);
        newCarSpot.setCarModel(request.carModel);
        newCarSpot.setSpotDate(LocalDateTime.now());
        carSpotRepository.save(newCarSpot);
    }

    public void editCarSpot(Long id, NewCarSpotRequest request){
        CarSpot updateCarSpot = carSpotRepository.findById(id).orElseThrow();
        updateCarSpot.setTitle(request.carSpotTitle);
        updateCarSpot.setCarManufacture(request.carManufacture);
        updateCarSpot.setCarModel(request.carModel);
        updateCarSpot.setSpotDate(LocalDateTime.now());
        carSpotRepository.save(updateCarSpot);
    }

    public void deleteCarSpot(Long id){
        carSpotRepository.deleteById(id);
    }

}
