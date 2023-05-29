package com.carspottingapp.repositories;

import com.carspottingapp.spottedCarModels.CarSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarSpotRepository extends JpaRepository<CarSpot, Long> {
}
