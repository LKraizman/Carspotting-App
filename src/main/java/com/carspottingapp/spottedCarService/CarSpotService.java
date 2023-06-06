package com.carspottingapp.spottedCarService;

import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.exception.InvalidLengthException;
import com.carspottingapp.repository.CarModelRepository;
import com.carspottingapp.repository.CarSpotRepository;
import com.carspottingapp.spottedCarModel.CarModel;
import com.carspottingapp.spottedCarModel.CarSpot;
import com.carspottingapp.spottedCarModel.response.CarSpotResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarSpotService {
    public final CarSpotRepository carSpotRepository;
    public final CarModelRepository carModelRepository;

    public List<CarSpotResponse> getCarSpots() {
        return carSpotRepository.findAll().stream().map(CarSpotResponse::new).toList();
    }

    public CarSpotResponse getCarSpotById(Long id) {
        return carSpotRepository.findById(id).map(CarSpotResponse::new).
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
        return new CarSpotResponse(saveSpot);
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
        updateCarSpot.setPictureUrl(request.photoLink);
        updateCarSpot.setCarModelId(request.carModelId);
        updateCarSpot.setSpotDate(request.spotDate);
        CarSpot updatedSpot = carSpotRepository.save(updateCarSpot);
        return new CarSpotResponse(updatedSpot);
    }

    public void deleteCarSpot(Long id) {
        carSpotRepository.findById(id).orElseThrow(InvalidIdException::new);
        carSpotRepository.deleteById(id);
    }
}
