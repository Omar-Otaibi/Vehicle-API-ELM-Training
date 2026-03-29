package org.example.vehicleapi.vehicle.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestTemplateTestController {

    private final RestTemplateTestService externalApiService;

    @GetMapping("/api/external-test")
    public ResponseEntity<List<ExternalVehicleDataDTO>> getExternalData() {
        // Calls the service, which calls the external URL, and returns it to the user
        return ResponseEntity.ok(externalApiService.fetchExternalData());
    }
}
