package org.example.vehicleapi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vehicles {
//    private int id;
    private String brand;
    private String model;
    private int year;
    private String plate;
    private String vin;
}
