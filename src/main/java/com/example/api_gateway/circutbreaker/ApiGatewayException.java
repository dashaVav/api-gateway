package com.example.api_gateway.circutbreaker;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiGatewayException {
    private String error;
    private int status;
}
