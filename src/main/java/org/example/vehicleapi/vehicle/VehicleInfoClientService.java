package org.example.vehicleapi.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class VehicleInfoClientService {
    private final RestTemplate restTemplate;

    public ExternalVehicleInfoDTO getExternalVehicleInfo(String vin) {
        String url = "https://69afdd7bc63dd197feba6b64.mockapi.io/ap/v4/restTemplateTest";
        try {
            return restTemplate.getForObject(url, ExternalVehicleInfoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch vehicle info from external service: " + e.getMessage());
        }
    }
}
