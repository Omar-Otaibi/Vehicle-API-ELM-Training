package org.example.vehicleapi.vehicle;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicles, Long>, JpaSpecificationExecutor<Vehicles> {
    List<Vehicles> findByPlateContaining(String plate);

    @EntityGraph(attributePaths = {"owner"})
    Optional<Vehicles> findByVin(String vin);
}
