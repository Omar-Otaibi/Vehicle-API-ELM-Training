package org.example.vehicleapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/addVehicle")
    public ResponseEntity<VehiclesDTO> createVehicle(@Valid @RequestBody VehiclesDTO vehiclesDTO) {
        VehiclesDTO savedVehicle = vehicleService.createVehicle(vehiclesDTO);
        return new ResponseEntity<>(savedVehicle,HttpStatus.CREATED);
    }

    @GetMapping("/getVehicles")
    public ResponseEntity<List<VehiclesDTO>> getVehicles() {
        return ResponseEntity.ok(vehicleService.getVehicles());

    }
}
