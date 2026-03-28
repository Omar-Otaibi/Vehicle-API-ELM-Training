package org.example.vehicleapi.vehicle.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RestTemplateTestService {
    private final RestTemplate restTemplate;

    private static final String EXTERNAL_API_URL = "https://69afdd7bc63dd197feba6b64.mockapi.io/ap/v4/Vehicle-data";

    public Object fetchExternalData(){
        try {
            return restTemplate.getForObject(EXTERNAL_API_URL, Object.class);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }
}
