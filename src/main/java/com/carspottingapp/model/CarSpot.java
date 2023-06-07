package com.carspottingapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_spots")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_spot_id")
    private Long carSpotId;

    @Column(name = "car_spot_title")
    private String carSpotTitle;

    @Column(name = "car_model_id")
    private Long carModelId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "car_model_id", insertable = false, updatable = false)
    private CarModel carModel;

    @Column(name = "spot_date")
    private LocalDateTime spotDate;

    @Column(name = "description")
    private String description;

    @Column(name = "picture_url")
    private String pictureUrl;

    public CarSpot(String carSpotTitle, String description, String pictureUrl, CarModel carModel, LocalDateTime spotDate) {
        this.carSpotTitle = carSpotTitle;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.carModelId = carModel.getCarModelId();
        this.carModel = carModel;
        this.spotDate = spotDate;
    }
}
