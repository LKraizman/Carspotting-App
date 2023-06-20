package com.carspottingapp.repository;

import com.carspottingapp.model.CarSpotUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarSpotUserRepository extends JpaRepository<CarSpotUser, Long> {
    Optional<CarSpotUser> findByEmail(String email);
}
