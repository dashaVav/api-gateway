package com.example.api_gateway.filter;

import com.example.api_gateway.dto.JwtAuthDTO;
import com.example.api_gateway.dto.TokenValidationDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AuthClient {
    private final WebClient webClient;

    public AuthClient(String baseUrl) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public JwtAuthDTO checkToken(TokenValidationDTO token, String path) {
        var post = webClient.post().uri(path)
                .body(BodyInserters.fromValue(token))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.empty()
                )
                .toEntity(JwtAuthDTO.class)
                .block();
        assert post != null;
        return post.getBody();
    }
}
