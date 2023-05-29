package com.carspottingapp.repositories;

import com.carspottingapp.spottedCarModels.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    List<CarModel> findAllByCarManufacturerId(Long id);
}

