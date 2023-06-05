package com.carspottingapp.spottedCarServices;

import com.carspottingapp.exceptions.InvalidIdException;
import com.carspottingapp.exceptions.InvalidLengthException;
import com.carspottingapp.repositories.CarModelRepository;
import com.carspottingapp.repositories.CarSpotRepository;
import com.carspottingapp.spottedCarModels.CarModel;
import com.carspottingapp.spottedCarModels.CarSpot;
import com.carspottingapp.spottedCarModels.responses.CarSpotResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarSpotService {
    public final CarSpotRepository carSpotRepository;
    public final CarModelRepository carModelRepository;

    public List<CarSpotResponse> getCarSpots() {
        return carSpotRepository.findAll().stream().map(carSpot ->
                new CarSpotResponse(
                        carSpot.getCarSpotId(),
                        carSpot.getCarSpotTitle(),
                        carSpot.getCarModel().getCarManufacturer().getCarManufacturer(),
                        carSpot.getCarModel().getCarModel(),
                        carSpot.getDescription(),
                        carSpot.getPhotoLink(),
                        carSpot.getSpotDate())).toList();
    }

    public CarSpotResponse getCarSpotById(Long id) {
        return carSpotRepository.findById(id).map(carSpot ->
                new CarSpotResponse(
                        carSpot.getCarSpotId(),
                        carSpot.getCarSpotTitle(),
                        carSpot.getCarModel().getCarManufacturer().getCarManufacturer(),
                        carSpot.getCarModel().getCarModel(),
                        carSpot.getDescription(),
                        carSpot.getPhotoLink(),
                        carSpot.getSpotDate())).
                orElseThrow(InvalidIdException::new);
    }

    public CarSpotResponse addCarSpot(NewCarSpotRequest request) throws InvalidLengthException {

        Optional<CarModel> byId = carModelRepository.findById(request.carModelId);

        CarSpot newCarSpot = byId.map(model -> new CarSpot(
                request.carSpotTitle,
                request.description,
                request.photoLink,
                model,
                LocalDateTime.now()))
                .orElseThrow(InvalidIdException::new);

        if (request.carSpotTitle.length() > 30) {
            throw new InvalidLengthException();
        } else if (request.description.length() > 300) {
            throw new InvalidLengthException();
        }
        CarSpot saveSpot = carSpotRepository.save(newCarSpot);
        return new CarSpotResponse(
                saveSpot.getCarSpotId(),
                saveSpot.getCarSpotTitle(),
                saveSpot.getCarModel().getCarManufacturer().getCarManufacturer(),
                saveSpot.getCarModel().getCarModel(),
                saveSpot.getDescription(),
                saveSpot.getPhotoLink(),
                saveSpot.getSpotDate());
    }

    public CarSpotResponse editCarSpot(Long id, NewCarSpotRequest request) throws InvalidLengthException {
        CarSpot updateCarSpot = carSpotRepository.findById(id).orElseThrow(InvalidIdException::new);
        if (request.carSpotTitle.length() > 30) {
            throw new InvalidLengthException();
        } else if (request.description.length() > 300) {
            throw new InvalidLengthException();
        }
        updateCarSpot.setCarSpotTitle(request.carSpotTitle);
        updateCarSpot.setDescription(request.description);
        updateCarSpot.setPhotoLink(request.photoLink);
        updateCarSpot.setCarModelId(request.carModelId);
        updateCarSpot.setSpotDate(request.spotDate);
        CarSpot updatedSpot = carSpotRepository.save(updateCarSpot);
        return new CarSpotResponse(
                updatedSpot.getCarSpotId(),
                updatedSpot.getCarSpotTitle(),
                updatedSpot.getCarModel().getCarManufacturer().getCarManufacturer(),
                updatedSpot.getCarModel().getCarModel(),
                updatedSpot.getDescription(),
                updatedSpot.getPhotoLink(),
                updatedSpot.getSpotDate());
    }

    public void deleteCarSpot(Long id) {
        carSpotRepository.findById(id).orElseThrow(InvalidIdException::new);
        carSpotRepository.deleteById(id);
    }
}
