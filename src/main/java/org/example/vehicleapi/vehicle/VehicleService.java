package org.example.vehicleapi.vehicle;

import jakarta.transaction.Transactional;
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
public class VehicleService {

    private final VehicleRepository repository;
    private final OwnerRepository ownerRepository;

    public VehicleService(VehicleRepository repository, OwnerRepository ownerRepository) {
        this.repository = repository;
        this.ownerRepository = ownerRepository;
    }

    public VehiclesDTO createVehicle(VehiclesDTO DTO) {
        var owner = ownerRepository.findById(DTO.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        var exists = repository.findByVin(DTO.vin());
        if(exists.isPresent()){
            throw new RuntimeException("vehicle duplicated");
        }
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
    public Page<VehiclesDTO> getVehicles(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::convertVehicleToDTO);
    }

    public List<VehiclesDTO> searchVehiclesByPlate(String plateQuery) {
        // Uses the new repository method to filter
        return repository.findByPlateContaining(plateQuery).stream()
                .map(this::convertVehicleToDTO)
                .toList();
    }
    public List<VehiclesDTO> getVehiclesSortedByBrand() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "brand")).stream()
                .map(this::convertVehicleToDTO)
                .toList();
    }
    public List<VehiclesDTO> getNewestVehicles() {
        return repository.findTop3ByOrderByYearDesc().stream()
                .map(this::convertVehicleToDTO)
                .toList();
    }

    // JPQL
    public List<VehiclesDTO> getVehiclesByBrandAndYear(String brand, int minYear) {
        return repository.findByBrandAndNewer(brand, minYear).stream()
                .map(this::convertVehicleToDTO)
                .toList();
    }

    //Native SQL
    public List<VehiclesDTO> getVehiclesByModel(String model) {
        return repository.findByModelNative(model).stream()
                .map(this::convertVehicleToDTO)
                .toList();
    }

    //Distinct
    public List<VehiclesDTO> getDistinctVehiclesByBrand(String brand) {
        return repository.findDistinctByBrand(brand).stream()
                .map(this::convertVehicleToDTO)
                .toList();
    }

    //Search by Owner Name
    public List<VehiclesDTO> getVehiclesByOwnerName(String firstName) {
        return repository.findByOwner_FirstName(firstName).stream()
                .map(this::convertVehicleToDTO)
                .toList();
    }

    public VehiclesDTO listByVin(String vin){
        Vehicles vehicle = repository.findByVin(vin).orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return  convertVehicleToDTO(vehicle);
    }

    private VehiclesDTO convertVehicleToDTO(Vehicles v) {
        return new VehiclesDTO(v.getId(), v.getBrand(), v.getModel(), v.getYear(),
                v.getPlate(), v.getVin(), v.getOwner() != null ? v.getOwner().getId() : null);
    }
    private UpdateVehicleDTO convertUpdatedVehicleToDTO(Vehicles v) {
        return new UpdateVehicleDTO(v.getBrand(), v.getModel(), v.getYear(),
                v.getPlate(), v.getVin(), v.getOwner() != null ? v.getOwner().getId() : null);
    }

    public UpdateVehicleDTO updateVehicle(Long id, UpdateVehicleDTO dto) {
        //check vehicle
        Vehicles existingVehicle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        //checks every field
        if (dto.brand() != null && !dto.brand().isBlank()) {
            existingVehicle.setBrand(dto.brand());
        }
        if (dto.model() != null && !dto.model().isBlank()) {
            existingVehicle.setModel(dto.model());
        }
        if (dto.year() != null) {
            existingVehicle.setYear(dto.year());
        }
        if (dto.plate() != null && !dto.plate().isBlank()) {
            existingVehicle.setPlate(dto.plate());
        }
        if (dto.vin() != null && !dto.vin().isBlank()) {
            existingVehicle.setVin(dto.vin());
        }

        if (dto.ownerId() != null) {
            var newOwner = ownerRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            existingVehicle.setOwner(newOwner);
        }
        Vehicles updatedVehicle = repository.save(existingVehicle);
        return convertUpdatedVehicleToDTO(updatedVehicle);
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
                .map(this::convertVehicleToDTO);
    }

    public void deleteVehicle(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Vehicle not found!");
        }
        repository.deleteById(id);
    }

}
