package com.logi_manage.auth_user_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
