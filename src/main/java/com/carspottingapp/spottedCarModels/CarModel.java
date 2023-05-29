package com.carspottingapp.spottedCarModels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carmodel")
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carmodelid")
    private Long carModelId;

    @Column(name = "carmanufacturerid")
    private Long carManufacturerId;

    @ManyToOne
    @JoinColumn(name = "carmanufacturerid", insertable = false, updatable = false)
    private CarManufacturer carManufacturer;

    @Column(name = "carmodel")
    private String carModel;
}
