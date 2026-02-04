package org.example.vehicleapi.owner;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import org.example.vehicleapi.vehicle.VehiclesDTO;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OwnerDTO(
                Long id,

                @NotBlank String firstName,

                @NotBlank String lastName,

                List<VehiclesDTO> vehicles) {
        public OwnerDTO(Long id, String firstName, String lastName) {
                this(id, firstName, lastName, null);
        }
}
