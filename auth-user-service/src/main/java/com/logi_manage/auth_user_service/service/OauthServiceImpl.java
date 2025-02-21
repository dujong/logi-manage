package com.logi_manage.auth_user_service.service;

import com.logi_manage.auth_user_service.dto.request.LoginReq;
import com.logi_manage.auth_user_service.dto.request.SignUpReq;
import com.logi_manage.auth_user_service.entity.Users;
import com.logi_manage.auth_user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OauthServiceImpl implements OauthService{
    private final UserRepository userRepository;
    @Override
    public void signUp(SignUpReq signUpReq) {
        Users users = new Users(null,
                signUpReq.username(),
                signUpReq.password(),
                signUpReq.name(),
                signUpReq.role(),
                null,
                null);

        userRepository.save(users);
    }

    @Override
    public Long login(LoginReq loginReq) {

        return null;
    }
}
