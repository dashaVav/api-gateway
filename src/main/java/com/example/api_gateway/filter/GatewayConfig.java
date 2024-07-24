package com.example.api_gateway.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public ConnectExceptionGlobalFilter connectExceptionGlobalFilter() {
        return new ConnectExceptionGlobalFilter();
    }

}