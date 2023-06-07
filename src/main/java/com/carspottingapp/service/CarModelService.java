package com.carspottingapp.service;

import com.carspottingapp.repository.CarModelRepository;
import com.carspottingapp.model.response.CarModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CarModelService {
    public final CarModelRepository carModelRepository;

    public List<CarModelResponse> getModelsByBrandId(Long id) {
        return carModelRepository.findAllByCarBrandId(id).stream().map(CarModelResponse::new).toList();
    }
}
