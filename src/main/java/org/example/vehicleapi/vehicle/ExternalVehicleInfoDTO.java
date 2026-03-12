package org.example.vehicleapi.vehicle;

public record ExternalVehicleInfoDTO(
        Long id,
        String vin,
        Double price,
        String status
) {}
