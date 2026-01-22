package org.example.vehicleapi;

import jakarta.validation.constraints.*;

public record VehiclesDTO(
        @NotBlank(message = "cannot be empty")
        String brand,

        @NotBlank(message = "cannot be empty")
        String model,

        @Min(value = 1900, message = "Year must be at least 1900")
        @Max(value = 2026, message = "Year cannot be in the future")
        Integer year,

        @NotNull
        String plate,

        @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
        @NotBlank(message = "VIN is required")
        String vin
)
{}
