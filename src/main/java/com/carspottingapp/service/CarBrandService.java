package com.carspottingapp.service;

import com.carspottingapp.repository.CarBrandRepository;
import com.carspottingapp.model.response.CarBrandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CarBrandService {
    public final CarBrandRepository carBrandRepository;

    public List<CarBrandResponse> getCarBrands() {
        return carBrandRepository.findAll().stream().map(CarBrandResponse::new).toList();
    }
}
