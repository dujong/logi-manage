package com.logi_manage.auth_user_service.service;

import com.logi_manage.auth_user_service.dto.request.LoginReq;
import com.logi_manage.auth_user_service.dto.request.SignUpReq;
import com.logi_manage.auth_user_service.dto.response.TokenRes;

public interface OauthService {
    void signUp(SignUpReq signUpReq);
    boolean checkEmailDuplicate(String email);
    TokenRes login(LoginReq loginReq);
}
