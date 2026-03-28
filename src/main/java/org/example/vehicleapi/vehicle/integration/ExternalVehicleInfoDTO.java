package org.example.vehicleapi.vehicle.integration;

public record ExternalVehicleInfoDTO(
        String brand,
        String model,
        Integer year,
        String plate,
        String vin,
        Long ownerId,

        // The external fields only exist in this specific response
        Double price,
        String status
) {}
