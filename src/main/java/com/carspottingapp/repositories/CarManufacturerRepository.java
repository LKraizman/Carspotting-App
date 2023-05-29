package com.carspottingapp.repositories;

import com.carspottingapp.spottedCarModels.CarManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarManufacturerRepository extends JpaRepository<CarManufacturer, Long> {
}
