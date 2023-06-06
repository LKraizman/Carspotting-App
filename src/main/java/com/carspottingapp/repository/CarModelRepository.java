package com.carspottingapp.repository;

import com.carspottingapp.spottedCarModel.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    List<CarModel> findAllByCarBrandId(Long id);
}

