package org.example.vehicleapi;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehicles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( length = 20, nullable = false)
    private String brand;
    @Column(length = 20)
    private String model;
    @Column(length = 4)
    private int year;
    @Column(length = 6, unique = true)
    private String plate;
    @Column(length = 17, unique = true)
    private String vin;
}
