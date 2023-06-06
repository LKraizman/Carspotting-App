package com.carspottingapp.repository;

import com.carspottingapp.spottedCarModel.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {
}
