package org.example.vehicleapi.owner;

import org.example.vehicleapi.vehicle.VehicleMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {VehicleMapper.class}, // Reuse vehicle mapping logic
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OwnerMapper {
    @Mapping(target = "vehicles", ignore = true)
    OwnerDTO toDTO(Owner owner);

    Owner toEntity(OwnerDTO dto);

    OwnerDTO toDetailedDTO(Owner owner);
}