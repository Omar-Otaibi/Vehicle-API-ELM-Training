package org.example.vehicleapi.vehicle;

import jakarta.transaction.Transactional;
import org.example.vehicleapi.owner.Owner;
import org.example.vehicleapi.owner.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleService {

    private final VehicleRepository repository;
    private final OwnerRepository ownerRepository;

    public VehicleService(VehicleRepository repository, OwnerRepository ownerRepository) {
        this.repository = repository;
        this.ownerRepository = ownerRepository;
    }

    public VehiclesDTO createVehicle(VehiclesDTO DTO) {
        Owner owner = ownerRepository.findById(DTO.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Vehicles vehicle = new Vehicles();
        vehicle.setBrand(DTO.brand());
        vehicle.setModel(DTO.model());
        vehicle.setYear(DTO.year());
        vehicle.setPlate(DTO.plate());
        vehicle.setVin(DTO.vin());
        vehicle.setOwner(owner);

        Vehicles save = repository.save(vehicle);
        return convertVehicleToDTO(save);
    }

    public List<VehiclesDTO> getVehicles() {
        return repository.findAll().stream()
                .map(this::convertVehicleToDTO)
                .collect(Collectors.toList());
    }

    private VehiclesDTO convertVehicleToDTO(Vehicles v) {
        return new VehiclesDTO(v.getId(), v.getBrand(), v.getModel(), v.getYear(),
                v.getPlate(), v.getVin(), v.getOwner() != null ? v.getOwner().getId() : null);
    }
}
