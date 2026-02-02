package org.example.vehicleapi.owner;

import org.example.vehicleapi.vehicle.VehiclesDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional()
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public OwnerDTO createOwner(OwnerDTO ownerDTO) {
        Owner owner = new Owner();
        owner.setFirstName(ownerDTO.firstName());
        owner.setLastName(ownerDTO.lastName());
        Owner save = ownerRepository.save(owner);
        return convertToDTO(save);
    }


    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public OwnerDTO getOwnerInfo(long ownerId) {
        return ownerRepository.findById(ownerId).map(this::convertToDTOInfo).orElse(null);
    }

    private OwnerDTO convertToDTO(Owner o) {
        return new OwnerDTO(
                o.getId(),
                o.getFirstName(),
                o.getLastName()
        );
    }

    private OwnerDTO convertToDTOInfo(Owner o) {
        return new OwnerDTO(
                o.getId(),
                o.getFirstName(),
                o.getLastName(),
                o.getVehicles().stream()
                        .map(v -> new VehiclesDTO(v.getId(), v.getBrand(), v.getModel(), v.getYear(),
                                v.getPlate(), v.getVin(), v.getOwner().getId()))
                        .toList());
    }

}
