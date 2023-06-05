package com.carspottingapp.spottedCarModels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "carspots")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carspotid")
    private Long carSpotId;

    @Column(name = "carspottitle")
    private String carSpotTitle;

    @Column(name = "carmodel_id")
    private Long carModelId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "carmodel_id", insertable = false, updatable = false)
    private CarModel carModel;

    @Column(name = "spotdate")
    private LocalDateTime spotDate;

    @Column(name = "description")
    private String description;

    @Column(name = "photolink")
    private String photoLink;

    public CarSpot(String carSpotTitle, String description, String photoLink, CarModel carModel, LocalDateTime spotDate) {
        this.carSpotTitle = carSpotTitle;
        this.description = description;
        this.photoLink = photoLink;
        this.carModelId = carModel.getCarModelId();
        this.carModel = carModel;
        this.spotDate = spotDate;
    }
}
