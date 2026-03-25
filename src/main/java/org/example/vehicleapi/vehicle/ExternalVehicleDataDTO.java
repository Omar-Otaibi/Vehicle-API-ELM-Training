package org.example.vehicleapi.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalVehicleDataDTO(
        @JsonProperty("id") String id,
        @JsonProperty("Price") Double price,
        @JsonProperty("Status") String status
) {}
