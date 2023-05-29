package com.carspottingapp.spottedCarServices;

import com.carspottingapp.repositories.CarManufacturerRepository;
import com.carspottingapp.spottedCarModels.responses.CarManufacturerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarManufacturerService {
    public final CarManufacturerRepository carManufacturerRepository;

    public CarManufacturerService(CarManufacturerRepository carManufacturerRepository) {
        this.carManufacturerRepository = carManufacturerRepository;
    }

    public List<CarManufacturerResponse> getCarManufacturer() {
        return carManufacturerRepository.findAll().stream().map(carManufacturer ->
                new CarManufacturerResponse
                        (carManufacturer.getCarManufacturerId(),
                                carManufacturer.getCarManufacturer())).toList();
    }
}
