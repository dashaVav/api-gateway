package com.example.api_gateway.circutbreaker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CircuitBreakerController {
    @RequestMapping("/fallback")
    public ResponseEntity<ApiGatewayException> fallback() {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        return new ResponseEntity<>(
                new ApiGatewayException("Service Unavailable", status.value()),
                status);
    }
}
