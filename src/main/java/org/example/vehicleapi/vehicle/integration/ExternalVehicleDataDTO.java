package org.example.vehicleapi.vehicle.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalVehicleDataDTO(
        String id,
        @JsonProperty("Price") Double price,
        @JsonProperty("Status") String status
) {}
