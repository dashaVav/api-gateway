package com.example.api_gateway.filter;

import com.example.api_gateway.dto.JwtAuthDTO;
import com.example.api_gateway.dto.TokenValidationDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ServerHttpBearerAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

    private static final String BEARER = "Bearer ";
    private static final Predicate<String> matchBearerLength = authValue -> authValue.length() > BEARER.length();
    private static final Function<String, Mono<String>> isolateBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length()));

    public Mono<String> extract(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }

    @Override
    public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(this::extract)
                .filter(matchBearerLength)
                .flatMap(isolateBearerValue)
                .flatMap(this::check);
    }


    private Mono<Authentication> check(String token) {
        var res = client(token);
        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(res.getUsername(), null,
                List.of((GrantedAuthority) () -> res.getRole().toString())));
    }

    private JwtAuthDTO client(String token) {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8096").build();
        var post = client.post().uri("/auth/check")
                .body(BodyInserters.fromValue(new TokenValidationDTO(token)))
                .retrieve()
                .toEntity(JwtAuthDTO.class)
                .block();
        return post.getBody();
    }
}
