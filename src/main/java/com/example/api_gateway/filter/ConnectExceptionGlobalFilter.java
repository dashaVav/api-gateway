package com.example.api_gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

public class ConnectExceptionGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).onErrorResume(e -> {
            if (e instanceof ConnectException || e.getCause() instanceof ConnectException) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
            }
            return Mono.error(e);
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
