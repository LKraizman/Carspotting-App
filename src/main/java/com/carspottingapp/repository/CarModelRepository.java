package com.carspottingapp.repository;

import com.carspottingapp.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    List<CarModel> findAllByCarBrandId(Long id);
}

