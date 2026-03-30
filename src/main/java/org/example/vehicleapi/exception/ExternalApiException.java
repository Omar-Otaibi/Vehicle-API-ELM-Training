package org.example.vehicleapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ExternalApiException extends RuntimeException {

    private final HttpStatus status;

    public ExternalApiException(HttpStatus status, String methodKey) {
        super("External API error [" + status.value() + " " + status.getReasonPhrase() + "] on " + methodKey);
        this.status = status;
    }

}