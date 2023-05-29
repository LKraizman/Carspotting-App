package com.carspottingapp.spottedCarModels;

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
@Table(name = "carmanufacturer")
public class CarManufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carmanufacturerid")
    private Long carManufacturerId;
    @Column(name = "carmanufacturer")
    private String carManufacturer;
    @OneToMany(mappedBy = "carManufacturer")
    private List<CarModel> models;
}
