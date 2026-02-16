package org.example.vehicleapi.vehicle;

import org.example.vehicleapi.owner.Owner;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VehicleMapper {

    // Entity -> DTO
    @Mapping(source = "owner.id", target = "ownerId")
    VehiclesDTO toDTO(Vehicles vehicle);

    // DTO + Owner -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    Vehicles toEntity(VehiclesDTO dto, Owner owner);

    // Partial Update (Ignore nulls in DTO)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    void updateVehicleFromDto(UpdateVehicleDTO dto, @MappingTarget Vehicles entity);

    // Entity -> Update DTO (For returning the result)
    @Mapping(source = "owner.id", target = "ownerId")
    UpdateVehicleDTO toUpdateDTO(Vehicles vehicle);
}