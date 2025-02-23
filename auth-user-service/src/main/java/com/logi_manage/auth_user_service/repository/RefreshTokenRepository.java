package com.logi_manage.auth_user_service.repository;

import com.logi_manage.auth_user_service.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken);
}
