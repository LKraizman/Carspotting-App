package com.carspottingapp.spottedCarServices;

import com.carspottingapp.repositories.CarModelRepository;
import com.carspottingapp.spottedCarModels.responses.CarManufacturerResponse;
import com.carspottingapp.spottedCarModels.responses.CarModelResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CarModelService {
    public final CarModelRepository carModelRepository;

    public CarModelService(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public List<CarModelResponse> getModels(Long id) {
        return carModelRepository.findAllByCarManufacturerId(id).stream().map(carModel ->
                new CarModelResponse(
                        carModel.getCarModelId(),
                        carModel.getCarModel())).toList();
    }
}
