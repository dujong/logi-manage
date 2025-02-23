package com.logi_manage.auth_user_service.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "token", timeToLive = 604800)
@NoArgsConstructor
@Getter
@ToString
public class Token {
    @Id
    private Long id;

    @Indexed
    private String accessToken;

    private String refreshToken;

    public Token(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
