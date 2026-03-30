package org.example.vehicleapi.vehicle.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestTemplateTestService {
    private final RestTemplate restTemplate;

    private static final String EXTERNAL_API_URL = "https://69afdd7bc63dd197feba6b64.mockapi.io/ap/v4/Vehicle-data";

    public List<ExternalVehicleDataDTO> fetchExternalData() {
        try {
            return restTemplate.exchange(
                    EXTERNAL_API_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ExternalVehicleDataDTO>>() {}
            ).getBody();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("External API call failed [" + e.getStatusCode() + "]: "
                    + e.getResponseBodyAsString(), e);
        }
    }
}
