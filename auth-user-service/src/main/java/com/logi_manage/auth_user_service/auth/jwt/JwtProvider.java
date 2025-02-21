package com.logi_manage.auth_user_service.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.logi_manage.auth_user_service.entity.Users;
import com.logi_manage.auth_user_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@Transactional
@RequiredArgsConstructor
@Service
public class JwtProvider {
    //config
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    //value
    private final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final String BEARER = "Bearer ";

    //repository
    private final UserRepository userRepository;

    /**
     * AccessToken 발급
     */
    public String issueAccessToken(Users users) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withClaim("userId", users.getId())
                .withClaim("role", users.getRole().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 발급
     */
    public String issueRefreshToken(Users users) {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withClaim("userId", users.getId())
                .withClaim("role", users.getRole().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 재발급
     */
    public String reissueAccessToken(String refreshToken) {
        DecodedJWT decodedJWT = verifyToken(refreshToken);

        if (decodedJWT == null) {
            throw new IllegalArgumentException("Invalid or expried RefreshToken");
        }

        Long userId = decodedJWT.getClaim("userId").asLong();
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return issueAccessToken(users);
    }

    /**
     * JWT 토큰 인증
     */
    public DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey))
                    .build().verify(token);
        } catch (Exception e) {
            log.error("Invalid token", e);
            return null;
        }
    }

    /**
     * Request Header token Get
     */
    public String extractToken(HttpServletRequest request) {
        Optional<String> accessToken = extractTokenFromHeader(request, accessHeader);
        if (accessToken.isPresent()) {
            return accessToken.get();
        }

        Optional<String> refreshToken = extractTokenFromHeader(request, refreshHeader);
        if (refreshToken.isPresent()) {
            return refreshToken.get();
        }

        throw new IllegalArgumentException("Request header does not contain a valid access or refresh token");
    }

    private Optional<String> extractTokenFromHeader(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(header -> header.startsWith(BEARER))
                .map(header -> header.replace(BEARER, ""));
    }
}
