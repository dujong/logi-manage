package com.logi_manage.auth_user_service.service;

import com.logi_manage.auth_user_service.dto.request.LoginReq;
import com.logi_manage.auth_user_service.dto.request.SignUpReq;

public interface OauthService {
    void signUp(SignUpReq signUpReq);

    Long login(LoginReq loginReq);
}
