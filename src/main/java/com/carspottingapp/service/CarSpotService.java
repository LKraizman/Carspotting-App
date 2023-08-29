package com.carspottingapp.service;

import com.carspottingapp.configuration.PropertyConfig;
import com.carspottingapp.exception.InvalidIdException;
import com.carspottingapp.exception.InvalidLengthException;
import com.carspottingapp.repository.CarModelRepository;
import com.carspottingapp.repository.CarSpotRepository;
import com.carspottingapp.model.CarModel;
import com.carspottingapp.model.CarSpot;
import com.carspottingapp.model.response.CarSpotResponse;
import com.carspottingapp.service.request.NewCarSpotRequest;
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
    public final PropertyConfig propertyConfig;

    public List<CarSpotResponse> getCarSpots() {
        return carSpotRepository.findAll().stream().map(CarSpotResponse::new).toList();
    }

    public CarSpotResponse getCarSpotById(Long id) {
        return carSpotRepository.findById(id).map(CarSpotResponse::new).
                orElseThrow(InvalidIdException::new);
    }

    public CarSpotResponse addCarSpot(NewCarSpotRequest request) throws InvalidLengthException {
        if (request.carSpotTitle.length() > Integer.parseInt(propertyConfig.getTitleLimit())) {
            throw new InvalidLengthException();
        } else if (request.description.length() > Integer.parseInt(propertyConfig.getDescriptionLimit())) {
            throw new InvalidLengthException();
        }

        Optional<CarModel> carModelById = carModelRepository.findById(request.carModelId);

        CarSpot newCarSpot = carModelById.map(model -> new CarSpot(
                        request.carSpotTitle,
                        request.description,
                        request.pictureUrl,
                        model,
                        LocalDateTime.now()))
                .orElseThrow(InvalidIdException::new);

        CarSpot saveSpot = carSpotRepository.save(newCarSpot);
        return new CarSpotResponse(saveSpot);
    }

    public CarSpotResponse editCarSpot(Long id, NewCarSpotRequest request) throws InvalidLengthException {
        if (request.carSpotTitle.length() > Integer.parseInt(propertyConfig.getTitleLimit())) {
            throw new InvalidLengthException();
        } else if (request.description.length() > Integer.parseInt(propertyConfig.getDescriptionLimit())) {
            throw new InvalidLengthException();
        }

        CarSpot updateCarSpot = carSpotRepository.findById(id).orElseThrow(InvalidIdException::new);

        updateCarSpot.setCarSpotTitle(request.carSpotTitle);
        updateCarSpot.setDescription(request.description);
        updateCarSpot.setPictureUrl(request.pictureUrl);
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
