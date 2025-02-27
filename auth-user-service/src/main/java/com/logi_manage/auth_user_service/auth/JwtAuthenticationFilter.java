package com.logi_manage.auth_user_service.auth;

import com.logi_manage.auth_user_service.constant.TokenValid;
import com.logi_manage.auth_user_service.entity.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 토큰 추출
        String accessToken = jwtProvider.extractTokenToRequestHeader(request);
        TokenValid accessTokenValid = jwtProvider.verifyToken(accessToken);

        //accessToken 유효
        if (accessTokenValid == TokenValid.VALID) {
            setAuthentication(accessToken);
        } else if (accessTokenValid == TokenValid.TIMEOUT) {
            handleTimeoutToken(response, accessToken);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleTimeoutToken(HttpServletResponse response, String accessToken) {
        log.info("AccessToken reissue");

        Token refreshToken = jwtProvider.getRefreshToken(accessToken);
        if (jwtProvider.verifyToken(refreshToken.getRefreshToken()) == TokenValid.VALID) {
            String newAccessToken = jwtProvider.reissueAccessToken(accessToken, refreshToken);
            setAuthentication(newAccessToken);
            setNewAccessTokenHeader(response, newAccessToken);
        }
    }

    private void setNewAccessTokenHeader(HttpServletResponse response, String newAccessToken) {
        response.setHeader("Authorization", "Bearer " + newAccessToken);
    }
}
