package com.logi_manage.auth_user_service.dto.request;


import com.logi_manage.auth_user_service.constant.Role;
import jakarta.validation.constraints.NotBlank;

public record SignUpReq(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        Role role
) {
}
