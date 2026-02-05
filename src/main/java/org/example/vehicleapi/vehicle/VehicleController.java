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

    @GetMapping("/searchByVin")
    public ResponseEntity<VehiclesDTO> searchByVin(@RequestParam String vin) {
        return ResponseEntity.ok(vehicleService.listByVin(vin));
    }
    //vehicle id
    @PatchMapping("/update/{id}")
    public ResponseEntity<UpdateVehicleDTO> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleDTO vehiclesDTO) {

        //pass the partial DTO to the service
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehiclesDTO));
    }

    @GetMapping("/searchVehicles")
    public ResponseEntity<Page<VehiclesDTO>> getVehicles(
            @RequestParam(required = false) String search, // General search (brand/model/vin)
            @RequestParam(required = false) Integer year,  // Specific filter
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String ownerName,
            Pageable pageable) {

        return ResponseEntity.ok(vehicleService.searchVehicles(search, year, plate,ownerName, pageable));
    }

    @DeleteMapping("/deleteVehicle/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return  ResponseEntity.noContent().build();
    }

    //Top 3 Newest Cars
    @GetMapping("/vehicles/newest")
    public ResponseEntity<List<VehiclesDTO>> getNewestVehicles() {
        return ResponseEntity.ok(vehicleService.getNewestVehicles());
    }

    //(JPQL)
    @GetMapping("/vehicles/filter")
    public ResponseEntity<List<VehiclesDTO>> filterByBrandAndYear(
            @RequestParam String brand,
            @RequestParam int year) {
        return ResponseEntity.ok(vehicleService.getVehiclesByBrandAndYear(brand, year));
    }

    //Native SQL Search
    @GetMapping("/vehicles/model/{model}")
    public ResponseEntity<List<VehiclesDTO>> getByModelNative(@PathVariable String model) {
        return ResponseEntity.ok(vehicleService.getVehiclesByModel(model));
    }

    //Distinct Search
    @GetMapping("/vehicles/distinct/{brand}")
    public ResponseEntity<List<VehiclesDTO>> getDistinctByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(vehicleService.getDistinctVehiclesByBrand(brand));
    }

    //Search by Owner First Name
    @GetMapping("/vehicles/owner/{name}")
    public ResponseEntity<List<VehiclesDTO>> getByOwnerName(@PathVariable String name) {
        return ResponseEntity.ok(vehicleService.getVehiclesByOwnerName(name));
    }
}
