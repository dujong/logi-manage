package com.logi_manage.auth_user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginInfo {
    private String email;
    private String password;
}
