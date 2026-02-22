package org.example.vehicleapi.vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicles, Long>, JpaSpecificationExecutor<Vehicles> {
    List<Vehicles> findByPlateContaining(String plate);

    @EntityGraph(attributePaths = {"owner"})
    Optional<Vehicles> findByVin(String vin);

    List<Vehicles> findTop3ByOrderByYearDesc();

    //JPQL using Entity name
    @Query("SELECT v FROM Vehicles v WHERE v.brand = :brand AND v.year >= :year")
    List<Vehicles> findByBrandAndNewer(@Param("brand") String brand, @Param("year") int year);

    //Native Query Search by model using the actual table name 'vehicles'
    @Query(value = "SELECT * FROM vehicles WHERE model LIKE %:model%", nativeQuery = true)
    List<Vehicles> findByModelNative(@Param("model") String model);

    List<Vehicles> findDistinctByBrand(String brand);

    //Nested Property: Find vehicles by the Owner's First Name
    //Vehicle -> Owner -> FirstName
    List<Vehicles> findByOwner_FirstName(String firstName);
}
