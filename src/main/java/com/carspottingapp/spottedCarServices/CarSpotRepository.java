package com.carspottingapp.spottedCarServices;

import com.carspottingapp.spottedCar.CarSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarSpotRepository extends JpaRepository<CarSpot, Long> {

}