package com.logi_manage.auth_user_service.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.logi_manage.auth_user_service.constant.TokenValid;
import com.logi_manage.auth_user_service.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 토큰 추출
        String accessToken = jwtProvider.extractTokenToRequestHeader(request);
        TokenValid accessTokenValid = jwtProvider.verifyToken(accessToken);

        //accessToken 유효
        if (accessTokenValid == TokenValid.VALID) {

        }

        try {


            //토큰 유효성 검사 및 인증
            if (token != null && jwtProvider.verifyToken(token) != null) {
                DecodedJWT decodedJWT = jwtProvider.verifyToken(token);
                Long userId = decodedJWT.getClaim("userId").asLong();
                String role = decodedJWT.getClaim("role").asString();

                userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

            }

        } catch (Exception e) {
            log.error("Authentication filed: ", e);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleTimeoutToken(HttpServletRequest request, HttpServletResponse response, String accessToken) {

    }
}
