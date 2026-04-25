package org.example.vehicleapi.vehicle.repo;
import org.example.vehicleapi.vehicle.entities.Vehicle;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    @EntityGraph(attributePaths = {"owner"})
    Optional<Vehicle> findById(Long id);

    List<Vehicle> findByPlateContaining(String plate);

    @EntityGraph(attributePaths = {"owner"})
    Optional<Vehicle> findByVin(String vin);

    List<Vehicle> findTop3ByOrderByYearDesc();

    //JPQL using Entity name
    @Query("SELECT v FROM Vehicle v WHERE v.brand = :brand AND v.year >= :year")
    List<Vehicle> findByBrandAndNewer(@Param("brand") String brand, @Param("year") int year);

    //Native Query Search by model using the actual table name 'vehicles'
    @Query(value = "SELECT * FROM vehicles WHERE model LIKE %:model%", nativeQuery = true)
    List<Vehicle> findByModelNative(@Param("model") String model);

    List<Vehicle> findDistinctByBrand(String brand);

    //Nested Property: Find vehicles by the Owner's First Name
    //Vehicle -> Owner -> FirstName
    List<Vehicle> findByOwner_FirstName(String firstName);

    @Modifying
    @Query("DELETE FROM Vehicle v WHERE v.year < :year")
    void deleteVehiclesOlderThan(@Param("year") int year);
}
