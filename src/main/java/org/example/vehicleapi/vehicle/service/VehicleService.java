package org.example.vehicleapi.vehicle.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vehicleapi.exception.ExternalApiException;
import org.example.vehicleapi.exception.VehicleNotFoundException;
import org.example.vehicleapi.owner.OwnerRepository;
import org.example.vehicleapi.vehicle.entities.Vehicle;
import org.example.vehicleapi.vehicle.repo.VehicleRepository;
import org.example.vehicleapi.vehicle.dto.UpdateVehicleDTO;
import org.example.vehicleapi.vehicle.dto.VehiclesDTO;
import org.example.vehicleapi.vehicle.integration.ExternalVehicleDataDTO;
import org.example.vehicleapi.vehicle.integration.ExternalVehicleInfoDTO;
import org.example.vehicleapi.vehicle.integration.MockApiFeignClient;
import org.example.vehicleapi.vehicle.mapper.VehicleMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository repository;
    private final OwnerRepository ownerRepository;
    private final VehicleMapper vehicleMapper;
    private final MockApiFeignClient feignClient;

    @Caching(
            put = {@CachePut(cacheNames = "vehicles", key = "#result.vin()")},
            evict = {@CacheEvict(value = {"vehicleStatus", "vehicles-by-brand-year", "newest-vehicles"}, allEntries = true)}
    )
    public VehiclesDTO createVehicle(VehiclesDTO DTO) {
        var owner = ownerRepository.findById(DTO.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        var exists = repository.findByVin(DTO.vin());
        if(exists.isPresent()){
            throw new RuntimeException("vehicle duplicated");
        }

        Vehicle vehicle = vehicleMapper.toEntity(DTO, owner);

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

    @Cacheable(value = "newest-vehicles")
    public List<VehiclesDTO> getNewestVehicles() {
        return repository.findTop3ByOrderByYearDesc().stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    // JPQL
    @Cacheable(value = "vehicles-by-brand-year", key = "#brand + '-' + #year")
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

    @Cacheable(value = "vehicles", key = "#vin", condition = "#year >= 2020")
    public VehiclesDTO listByVin(String vin){
        Vehicle vehicle = repository.findByVin(vin).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));
        return  vehicleMapper.toDTO(vehicle);
    }

    @CacheEvict(value = {"vehicles", "vehicleStatus", "vehicles-by-brand-year", "newest-vehicles"}, allEntries = true)
    public UpdateVehicleDTO updateVehicle(Long id, UpdateVehicleDTO dto) throws AccessDeniedException {
        //check vehicle
        Vehicle existingVehicle = repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));

        String currentUserEmail = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        // Check if the vehicle's owner email matches the logged-in user's email
        if (!existingVehicle.getOwner().getEmail().equals(currentUserEmail)) {
            // If they don't match, instantly block the request!
            throw new AccessDeniedException("Forbidden: You do not have permission to modify a vehicle you do not own.");
        }

        vehicleMapper.updateVehicleFromDto(dto, existingVehicle);

        if (dto.ownerId() != null) {
            var newOwner = ownerRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            existingVehicle.setOwner(newOwner);
        }
        Vehicle updatedVehicle = repository.save(existingVehicle);
        return vehicleMapper.toUpdateDTO(updatedVehicle);
    }

    public Page<VehiclesDTO> searchVehicles(String search, Integer year, String plate,String ownerName, Pageable pageable) {
        // Start with an empty specification
        Specification<Vehicle> spec = Specification.unrestricted();

        // Chain the rules dynamically
        if (search != null) spec = spec.and(Objects.requireNonNull(VehicleSpecs.filterByFields(search)));
        if (year != null) spec = spec.and(VehicleSpecs.hasYear(year));
        if (plate != null) spec = spec.and(VehicleSpecs.plateContains(plate));
        if (ownerName != null) spec = spec.and(VehicleSpecs.hasOwnerName(ownerName));

        // Execute query with Filter + Pagination + Sort
        return repository.findAll(spec, pageable)
                .map(vehicleMapper::toDTO);
    }

    @CacheEvict(value = {"vehicles", "vehicleStatus", "vehicles-by-brand-year", "newest-vehicles"}, allEntries = true,
    beforeInvocation = true)
    public void deleteVehicle(Long id) {
        if (!repository.existsById(id)) {
            throw new VehicleNotFoundException("Vehicle not found!");
        }
        repository.deleteById(id);
    }

    /** Delay Test
     * Security risk if non ownership calls the method after bring cached
     * the method runs first time and stored in cache
     * Second time it skips the method ownership check and found in cache
    */
    @Cacheable(value = "vehicleStatus", key = "#id", unless = "#result.status == 'API_UNAVAILABLE'")
    public ExternalVehicleInfoDTO getVehicleStatus(Long id) throws AccessDeniedException {
        //Fetch local vehicle
        Vehicle vehicle = repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));

        //Resource-Based Authorization
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!vehicle.getOwner().getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException("Forbidden: You do not have permission to view this vehicle's status.");
        }

        //Fetch external data
        ExternalVehicleDataDTO externalData;
        try {
            externalData = feignClient.fetchVehicleStatusById(id);
        } catch (ExternalApiException e) {
            log.warn("External API unavailable for vehicle id={}: {}", id, e.getMessage());
            externalData = new ExternalVehicleDataDTO(String.valueOf(id), 0L, "API_UNAVAILABLE");
        }

        return vehicleMapper.toVehicleStatusDTO(vehicle, externalData);
    }
}
