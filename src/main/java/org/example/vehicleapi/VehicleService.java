package org.example.vehicleapi;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {
    private final List<Vehicles> vehicleList = new ArrayList<>();

    public VehiclesDTO createVehicle(VehiclesDTO DTO) {
        Vehicles vehicle = new Vehicles(
                DTO.brand(),
                DTO.model(),
                DTO.year(),
                DTO.plate(),
                DTO.vin()
        );
        vehicleList.add(vehicle);
        return DTO;
    }

    public List<VehiclesDTO> getVehicles() {
        return vehicleList.stream().map(v -> new VehiclesDTO(v.getBrand(),
                v.getModel(), v.getYear(), v.getPlate(), v.getVin())).toList();
    }

}
