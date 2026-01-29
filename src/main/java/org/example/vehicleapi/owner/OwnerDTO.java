package org.example.vehicleapi.owner;

import jakarta.validation.constraints.NotBlank;
import org.example.vehicleapi.vehicle.VehiclesDTO;

import java.util.List;

public record OwnerDTO(
        Long id,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        List<VehiclesDTO> vehicles
) {}
