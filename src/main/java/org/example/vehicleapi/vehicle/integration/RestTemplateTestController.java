package org.example.vehicleapi.vehicle.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestTemplateTestController {

    private final RestTemplateTestService externalApiService;

    @GetMapping("/api/external-test")
    public ResponseEntity<Object> getExternalData() {
        // Calls the service, which calls the external URL, and returns it to the user
        Object data = externalApiService.fetchExternalData();
        return ResponseEntity.ok(data);
    }
}
