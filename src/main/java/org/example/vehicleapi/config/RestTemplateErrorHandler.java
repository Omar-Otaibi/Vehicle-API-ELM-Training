package org.example.vehicleapi.config;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        int statusCode = response.getStatusCode().value();
        HttpStatus status = HttpStatus.resolve(statusCode);

        if (status == null) {
            throw new RuntimeException("Unknown HTTP status: " + statusCode);
        }

        switch (status) {
            case BAD_REQUEST ->
                    throw new RuntimeException("Bad Request (400) calling [" + method + " " + url + "]: " + body);
            case UNAUTHORIZED ->
                    throw new RuntimeException("Unauthorized (401) calling [" + method + " " + url + "]: " + body);
            case FORBIDDEN ->
                    throw new RuntimeException("Forbidden (403) calling [" + method + " " + url + "]: " + body);
            case NOT_FOUND ->
                    throw new RuntimeException("Not Found (404) calling [" + method + " " + url + "]: " + body);
            default -> {
                if (status.is5xxServerError()) {
                    throw new RuntimeException("Server Error (" + statusCode + ") calling [" + method + " " + url + "]: " + body);
                }
                throw new RuntimeException("HTTP Error (" + statusCode + ") calling [" + method + " " + url + "]: " + body);
            }
        }
    }
}