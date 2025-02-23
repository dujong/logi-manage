package com.logi_manage.auth_user_service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.logi_manage.auth_user_service.constant.Role;
import com.logi_manage.auth_user_service.constant.TokenValid;
import com.logi_manage.auth_user_service.entity.Token;
import com.logi_manage.auth_user_service.entity.Users;
import com.logi_manage.auth_user_service.repository.RefreshTokenRepository;
import com.logi_manage.auth_user_service.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    private final RefreshTokenRepository tokenRepository;

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
    public String issueRefreshToken(Users users, String accessToken) {
        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withClaim("userId", users.getId())
                .withClaim("role", users.getRole().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
        tokenRepository.save(new Token(users.getId(), accessToken, refreshToken));
        return refreshToken;
    }

    /**
     * RefreshToken 재발급
     */
    public String reissueAccessToken(String originAccessToken, Token refreshToken) {
        Users users = userRepository
                .findById(refreshToken.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        String newAccessToken = issueAccessToken(users);

        //기존 refreshToken 삭제
        removeRefreshToken(originAccessToken);
        //new refreshToken 저장
        tokenRepository.save(new Token(users.getId(), newAccessToken, refreshToken.getRefreshToken()));
        return newAccessToken;
    }

    /**
     * Remove RefreshToken
     */

    public void removeRefreshToken(String accessToken) {
        tokenRepository.findByAccessToken(accessToken).ifPresent(refreshToken -> tokenRepository.delete(refreshToken));
    }

    /**
     * JWT 토큰 인증
     */
    public TokenValid verifyToken(String token) {
        TokenValid tokenValid;
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            log.info("Token is Valid");
            tokenValid = TokenValid.VALID;
        } catch (TokenExpiredException e) {
            log.info("Token is Timeout");
            tokenValid = TokenValid.TIMEOUT;
        } catch (JWTVerificationException e) {
            log.info("Token is Unsupported");
            tokenValid = TokenValid.UNSUPPORTED;
        } catch (Exception e) {
            log.info("Token Exception");
            tokenValid = TokenValid.EX;
        }
        return tokenValid;
    }

    /**
     * Request Header token Get
     */
    public String extractTokenToRequestHeader(HttpServletRequest request) {
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

    /**
     * Cookie token Get
     */
    public String extractTokenToRequestCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Bearer ".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * Get Authentication
     */

    public Authentication getAuthentication(String accessToken) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken);
        Long userId = verify.getClaim("userId").asLong();
        String role = verify.getClaim("userId").asString();

        return new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
    }

    private Optional<String> extractTokenFromHeader(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(header -> header.startsWith(BEARER))
                .map(header -> header.replace(BEARER, ""));
    }


}
