package org.example.vehicleapi.vehicle;

import jakarta.validation.constraints.*;

public record VehiclesDTO(
//        Long id,

        @NotBlank(message = "cannot be empty")
        String brand,

        @NotBlank(message = "cannot be empty")
        String model,

        @Min(value = 1900, message = "Year must be at least 1900")
        @Max(value = 2026, message = "Year cannot be in the future")
        Integer year,

        @NotBlank
        String plate,

        @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
        @NotBlank(message = "VIN is required")
        String vin,

        @NotNull
        Long ownerId

)
{}
