package org.example.vehicleapi.vehicle.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mockApiClient", url = "https://69afdd7bc63dd197feba6b64.mockapi.io")
public interface MockApiFeignClient {// Passes the local vehicle ID to the end of the MockAPI URL
    @GetMapping("/ap/v4/Vehicle-data/{id}")
    ExternalVehicleDataDTO fetchVehicleStatusById(@PathVariable("id") Long id);
}
