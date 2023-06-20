package com.carspottingapp.сontroller;

import com.carspottingapp.model.CarSpotUser;
import com.carspottingapp.repository.CarSpotUserRepository;
import com.carspottingapp.service.CarSpotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class CarSpotUserController {
    private final CarSpotUserService carSpotUserService;

    @GetMapping
    public List<CarSpotUser> getUsers(){
        return carSpotUserService.getUsers();
    }
}
