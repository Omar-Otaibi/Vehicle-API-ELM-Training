package org.example.vehicleapi.owner;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import org.example.vehicleapi.vehicle.dto.VehiclesDTO;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OwnerDTO(
                Long id,

                @NotBlank String firstName,

                @NotBlank String lastName,

                @NotBlank String email,

                @NotBlank String password,

                List<VehiclesDTO> vehicles) {
        public OwnerDTO(Long id, String firstName, String lastName, String email, String password) {
                this(id, firstName, lastName, email, password, null);
        }
}
