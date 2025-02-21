package com.logi_manage.apigateway_service.auth;

import com.logi_manage.apigateway_service.auth.jwt.TokenProvider;
import com.netflix.spectator.impl.Config;
import io.netty.util.internal.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    private TokenProvider tokenProvider;

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(config.headerName);
            if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(config.granted + " ")) {
                String token = authorizationHeader.substring(7);
                try {
                    if (tokenProvider.isValidToken(token)) {
                        return chain.filter(exchange);
                    }
                } catch (Exception e) {
                    log.error("Token validation error: {}", e.getMessage());
                }
            }

            return unauthorizedResponse(exchange);
        });
    }

    //인증 실패 Response
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Getter
    @Setter
    public static class Config{
        private String headerName;
        private String granted;
    }
}
