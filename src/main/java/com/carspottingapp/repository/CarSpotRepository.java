package com.carspottingapp.repository;

import com.carspottingapp.model.CarSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarSpotRepository extends JpaRepository<CarSpot, Long> {
}
