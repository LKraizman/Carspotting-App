package com.carspottingapp.spottedCarService;

import com.carspottingapp.repository.CarModelRepository;
import com.carspottingapp.spottedCarModel.response.CarModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CarModelService {
    public final CarModelRepository carModelRepository;

    public List<CarModelResponse> getModelsByBrand(Long id) {
        return carModelRepository.findAllByCarBrandId(id).stream().map(CarModelResponse::new).toList();
    }
}
