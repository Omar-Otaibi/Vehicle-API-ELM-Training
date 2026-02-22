package org.example.vehicleapi.vehicle;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.vehicleapi.exception.VehicleNotFoundException;
import org.example.vehicleapi.owner.OwnerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository repository;
    private final OwnerRepository ownerRepository;
    private final VehicleMapper vehicleMapper;

    public VehiclesDTO createVehicle(VehiclesDTO DTO) {
        var owner = ownerRepository.findById(DTO.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        var exists = repository.findByVin(DTO.vin());
        if(exists.isPresent()){
            throw new RuntimeException("vehicle duplicated");
        }

        Vehicles vehicle = vehicleMapper.toEntity(DTO, owner);

        return vehicleMapper.toDTO(repository.save(vehicle));
    }

    public Page<VehiclesDTO> getVehicles(Pageable pageable) {
        return repository.findAll(pageable)
                .map(vehicleMapper::toDTO);
    }

    public List<VehiclesDTO> searchVehiclesByPlate(String plateQuery) {
        // Uses the new repository method to filter
        return repository.findByPlateContaining(plateQuery).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }
    public List<VehiclesDTO> getVehiclesSortedByBrand() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "brand")).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }
    public List<VehiclesDTO> getNewestVehicles() {
        return repository.findTop3ByOrderByYearDesc().stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    // JPQL
    public List<VehiclesDTO> getVehiclesByBrandAndYear(String brand, int minYear) {
        return repository.findByBrandAndNewer(brand, minYear).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    //Native SQL
    public List<VehiclesDTO> getVehiclesByModel(String model) {
        return repository.findByModelNative(model).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    //Distinct
    public List<VehiclesDTO> getDistinctVehiclesByBrand(String brand) {
        return repository.findDistinctByBrand(brand).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    //Search by Owner Name
    public List<VehiclesDTO> getVehiclesByOwnerName(String firstName) {
        return repository.findByOwner_FirstName(firstName).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    public VehiclesDTO listByVin(String vin){
        Vehicles vehicle = repository.findByVin(vin).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));
        return  vehicleMapper.toDTO(vehicle);
    }

    public UpdateVehicleDTO updateVehicle(Long id, UpdateVehicleDTO dto) {
        //check vehicle
        Vehicles existingVehicle = repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));

        vehicleMapper.updateVehicleFromDto(dto, existingVehicle);

        if (dto.ownerId() != null) {
            var newOwner = ownerRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            existingVehicle.setOwner(newOwner);
        }
        Vehicles updatedVehicle = repository.save(existingVehicle);
        return vehicleMapper.toUpdateDTO(updatedVehicle);
    }

    public Page<VehiclesDTO> searchVehicles(String search, Integer year, String plate,String ownerName, Pageable pageable) {
        // Start with an empty specification
        Specification<Vehicles> spec = Specification.unrestricted();

        // Chain the rules dynamically
        if (search != null) spec = spec.and(Objects.requireNonNull(VehicleSpecs.filterByFields(search)));
        if (year != null) spec = spec.and(VehicleSpecs.hasYear(year));
        if (plate != null) spec = spec.and(VehicleSpecs.plateContains(plate));
        if (ownerName != null) spec = spec.and(VehicleSpecs.hasOwnerName(ownerName));

        // Execute query with Filter + Pagination + Sort
        return repository.findAll(spec, pageable)
                .map(vehicleMapper::toDTO);
    }

    public void deleteVehicle(Long id) {
        if (!repository.existsById(id)) {
            throw new VehicleNotFoundException("Vehicle not found!");
        }
        repository.deleteById(id);
    }

}
