package org.example.vehicleapi.config;

import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import org.example.vehicleapi.exception.ExternalApiException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

    // HTTP Error codes and their corresponding messages
    private static final Map<Integer, String> ERROR_MESSAGES = Map.of(
            400, "Bad request sent to external API",
            401, "Unauthorized — check API credentials",
            403, "Forbidden — access denied by external API",
            404, "Resource not found on external API",
            429, "Rate limit reached on external API",
            503, "External API is currently unavailable"
    );

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; //BASIC, HEADERS, FULL
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            HttpStatus status = HttpStatus.resolve(response.status());

            String message = ERROR_MESSAGES.getOrDefault(
                    response.status(),
                    "Unexpected error from external API" //unhandled status
            );

            return new ExternalApiException(
                    status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR,
                    methodKey + " — " + message
            );
        };
    }

    @Bean
    public Request.Options requestOptions() { //timeout
        return new Request.Options(5, TimeUnit.SECONDS, 10, TimeUnit.SECONDS, true);
    }
}