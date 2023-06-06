package com.carspottingapp.repository;

import com.carspottingapp.spottedCarModel.CarSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarSpotRepository extends JpaRepository<CarSpot, Long> {
}
