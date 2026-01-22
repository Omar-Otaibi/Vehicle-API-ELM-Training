package org.example.vehicleapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicles {
    private String brand;
    private String model;
    private int year;
    private String plate;
    private String vin;
}
