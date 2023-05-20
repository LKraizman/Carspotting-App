package com.carspottingapp.spottedCarControllers;

import com.carspottingapp.spottedCar.CarSpot;
import com.carspottingapp.spottedCarServices.CarSpotService;
import com.carspottingapp.spottedCarServices.NewCarSpotRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/carspots")
public class CarSpotController {
    private final CarSpotService carSpotService;
    public CarSpotController(CarSpotService carSpotService) {
        this.carSpotService = carSpotService;
    }

    @PostMapping
    public void addCarSpot(@RequestBody NewCarSpotRequest request) {
        carSpotService.addCarSpot(request);
    }
    @GetMapping
    public List<CarSpot> getCarSpots() {
        return carSpotService.getCarSpots();
    }
    @PutMapping("{carSpotId}")
    public void editCarSpot(@PathVariable("carSpotId") Long id, @RequestBody NewCarSpotRequest request){
        carSpotService.editCarSpot(id,request);
    }
    @DeleteMapping("{carSpotId}")
    public void deleteCarSpot(@PathVariable("carSpotId") Long id){
        carSpotService.deleteCarSpot(id);
    }
}
