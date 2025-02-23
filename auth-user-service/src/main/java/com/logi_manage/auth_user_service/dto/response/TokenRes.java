package com.logi_manage.auth_user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRes {
    private String accessToken;
    private String refreshToken;
}
