package com.carspottingapp.spottedCarServices;

import com.carspottingapp.spottedCar.SpottedCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpottedCarRepository extends JpaRepository<SpottedCar, Integer> {

}
