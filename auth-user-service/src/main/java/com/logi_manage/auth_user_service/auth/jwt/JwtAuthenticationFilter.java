package com.logi_manage.auth_user_service.auth.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.logi_manage.auth_user_service.entity.Users;
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
        try {
            //헤더에서 토큰 추출
            String token = jwtProvider.extractToken(request);

            //토큰 유효성 검사 및 인증
            if (token != null && jwtProvider.verifyToken(token) != null) {
                DecodedJWT decodedJWT = jwtProvider.verifyToken(token);
                Long userId = decodedJWT.getClaim("userId").asLong();
                String role = decodedJWT.getClaim("role").asString();

                userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

                Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            log.error("Authentication filed: ", e);
        }

        filterChain.doFilter(request, response);
    }
}
