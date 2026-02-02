package org.example.vehicleapi.vehicle;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping("/getVehicles")
//    public ResponseEntity<List<VehiclesDTO>> getVehicles() {
//        return ResponseEntity.ok(vehicleService.getVehicles());
//    }

    @GetMapping("/getVehicles")
    public ResponseEntity<Page<VehiclesDTO>> getVehicles(@PageableDefault(size = 15) Pageable pageable) {
        return ResponseEntity.ok(vehicleService.getVehicles(pageable));
    }

    @GetMapping("/sortByBrand")
    public ResponseEntity<List<VehiclesDTO>> getVehiclesSortedByBrand() {
        return ResponseEntity.ok(vehicleService.getVehiclesSortedByBrand());
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehiclesDTO>> searchByPlate(@RequestParam String plate) {
        return ResponseEntity.ok(vehicleService.searchVehiclesByPlate(plate));
    }

    // N + 1 case problem  - - - - - - - - - - - - - - - - - -
    @GetMapping("/searchByVin")
    public ResponseEntity<Vehicles> searchByvin(@RequestParam String vin) {
        return ResponseEntity.ok(vehicleService.listByVin(vin));
    }
}
