package com.carspottingapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_brands")
public class CarBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_brand_id")
    private Long carBrandId;

    @Column(name = "car_brand")
    private String carBrandName;

    @OneToMany(mappedBy = "carBrand")
    private List<CarModel> models;
}
