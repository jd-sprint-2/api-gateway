package com.example.api_gateway.filter;

import com.example.api_gateway.service.JwtService;
import com.example.api_gateway.RouteValidator;
import com.example.api_gateway.exceptions.UnauthorizedException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (RouteValidator.isSecured.test(exchange.getRequest())) {
                validateAuthorizationHeader(exchange.getRequest().getHeaders());
            }

            return chain.filter(exchange);
        };
    }

    private void validateAuthorizationHeader(HttpHeaders headers) {
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new UnauthorizedException("Authorization header is missing");
        }

        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid authorization header value");
        }

        String token = extractToken(authorizationHeader);
        validateToken(token);
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private void validateToken(String token) {
        try {
            jwtService.validateToken(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }


    public static class Config {
    }
}
