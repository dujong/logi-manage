package com.logi_manage.auth_user_service.service;

import com.logi_manage.auth_user_service.auth.JwtProvider;
import com.logi_manage.auth_user_service.dto.request.LoginReq;
import com.logi_manage.auth_user_service.dto.request.SignUpReq;
import com.logi_manage.auth_user_service.dto.response.TokenRes;
import com.logi_manage.auth_user_service.entity.Users;
import com.logi_manage.auth_user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class OauthServiceImpl implements OauthService{
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Override
    public void signUp(SignUpReq signUpReq) {
        Users users = new Users(signUpReq, passwordEncoder);
        userRepository.save(users);
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public TokenRes login(LoginReq loginReq) {
        Users users = userRepository.findByEmail(loginReq.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        //password check
        boolean isMatch = passwordEncoder.matches(loginReq.password(), users.getPassword());
        if (!isMatch) {
            //password check fail
            throw new IllegalArgumentException("Invalid password.");
        }

        String accessToken = jwtProvider.issueAccessToken(users);
        String refreshToken = jwtProvider.issueRefreshToken(users, accessToken);
        return new TokenRes(accessToken, refreshToken);
    }
}
