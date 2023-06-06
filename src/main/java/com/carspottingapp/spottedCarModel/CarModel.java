package com.carspottingapp.spottedCarModel;

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
@Table(name = "car_models")
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_model_id")
    private Long carModelId;

    @Column(name = "car_brand_id")
    private Long carBrandId;

    @ManyToOne
    @JoinColumn(name = "car_brand_id", insertable = false, updatable = false)
    private CarBrand carBrand;

    @Column(name = "car_model")
    private String carModel;

}
