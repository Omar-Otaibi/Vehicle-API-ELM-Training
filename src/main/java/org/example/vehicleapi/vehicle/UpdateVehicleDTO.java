package org.example.vehicleapi.vehicle;

import jakarta.validation.constraints.*;

public record UpdateVehicleDTO(

        String brand,

        String model,

        @Min(value = 1900, message = "Year must be at least 1900")
        @Max(value = 2026, message = "Year cannot be in the future")
        Integer year,

        @Pattern(regexp = "^[A-Z0-9-]{1,8}$", message = "Invalid plate format")
        String plate,

        @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
        String vin,

        Long ownerId
) {}