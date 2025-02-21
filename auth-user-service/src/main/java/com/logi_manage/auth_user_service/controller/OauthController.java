package com.logi_manage.auth_user_service.controller;

import com.logi_manage.auth_user_service.dto.request.LoginReq;
import com.logi_manage.auth_user_service.dto.request.SignUpReq;
import com.logi_manage.auth_user_service.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class OauthController {
    private final OauthService oauthService;

    @PostMapping("/sign")
    public ResponseEntity<?> signup(@RequestBody SignUpReq signUpReq) {
        oauthService.signUp(signUpReq);
        return ResponseEntity.ok("signup success!");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        oauthService.login(loginReq);
        return ResponseEntity.ok("1");
    }
}
