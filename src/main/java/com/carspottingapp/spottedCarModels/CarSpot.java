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

    @ManyToOne
    @JoinColumn(name = "carmodel_id", insertable = false, updatable = false)
    private CarModel carModel;

    @Column(name = "spotdate")
    private LocalDateTime spotDate;

    public CarSpot(String carSpotTitle, Long carModelId, LocalDateTime spotDate) {
        this.carSpotTitle = carSpotTitle;
        this.carModelId = carModelId;
        this.spotDate = spotDate;
    }
}
