package org.example.vehicleapi.vehicle.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalVehicleDataDTO(
        String id,
        @JsonProperty("Price") Double price,
        @JsonProperty("Status") String status
) {}
