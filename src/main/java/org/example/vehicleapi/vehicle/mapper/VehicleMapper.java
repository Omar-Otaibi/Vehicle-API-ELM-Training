package org.example.vehicleapi.vehicle.mapper;

import org.example.vehicleapi.owner.Owner;
import org.example.vehicleapi.vehicle.entities.Vehicle;
import org.example.vehicleapi.vehicle.dto.UpdateVehicleDTO;
import org.example.vehicleapi.vehicle.dto.VehiclesDTO;
import org.example.vehicleapi.vehicle.integration.ExternalVehicleDataDTO;
import org.example.vehicleapi.vehicle.integration.ExternalVehicleInfoDTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VehicleMapper {

    // Entity -> DTO
    @Mapping(source = "owner.id", target = "ownerId")
    VehiclesDTO toDTO(Vehicle vehicle);

    // DTO + Owner -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    Vehicle toEntity(VehiclesDTO dto, Owner owner);

    // Partial Update (Ignore nulls in DTO)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    void updateVehicleFromDto(UpdateVehicleDTO dto, @MappingTarget Vehicle entity);

    // Entity -> Update DTO (For returning the result)
    @Mapping(source = "owner.id", target = "ownerId")
    UpdateVehicleDTO toUpdateDTO(Vehicle vehicle);

    @Mapping(source = "externalData.price", target = "price")
    @Mapping(source = "externalData.status", target = "status")
    @Mapping(source = "vehicle.owner.id", target = "ownerId")
    ExternalVehicleInfoDTO toVehicleStatusDTO(Vehicle vehicle, ExternalVehicleDataDTO externalData);
}