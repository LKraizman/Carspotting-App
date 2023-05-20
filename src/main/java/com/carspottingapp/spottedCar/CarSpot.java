package com.carspottingapp.spottedCar;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CarSpot {
    @Id
    @SequenceGenerator(
            name = "carspot_id_sequence",
            sequenceName = "carspot_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "carspot_id_sequence"
    )
    private Long carSpotId;
    private String carSpotTitle;
    private String carManufacture;
    private String carModel;
    private LocalDateTime spotDate;

    public CarSpot(String title,
                   String carManufacture,
                   String carModel,
                   LocalDateTime date) {
        this.carSpotTitle = title;
        this.carManufacture = carManufacture;
        this.carModel = carModel;
        this.spotDate = date;
    }

    public CarSpot() {

    }

    public Long getId() {
        return carSpotId;
    }

    public void setId(Long id) {
        this.carSpotId = id;
    }

    public String getTitle() {
        return carSpotTitle;
    }

    public void setTitle(String title) {
        this.carSpotTitle = title;
    }

    public String getCarManufacture() {
        return carManufacture;
    }

    public void setCarManufacture(String carManufacture) {
        this.carManufacture = carManufacture;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public LocalDateTime getSpotDate() {
        return spotDate;
    }

    public void setSpotDate(LocalDateTime spotDate) {
        this.spotDate = spotDate;
    }
}
