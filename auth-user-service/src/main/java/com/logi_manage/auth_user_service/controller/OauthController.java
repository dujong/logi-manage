package com.logi_manage.auth_user_service.controller;

import com.logi_manage.auth_user_service.dto.request.LoginReq;
import com.logi_manage.auth_user_service.dto.request.SignUpReq;
import com.logi_manage.auth_user_service.dto.response.TokenRes;
import com.logi_manage.auth_user_service.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(oauthService.checkEmailDuplicate(email));
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        TokenRes tokenRes = oauthService.login(loginReq);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " +tokenRes.getAccessToken());
        httpHeaders.add(HttpHeaders.SET_COOKIE, "refreshToken=" + tokenRes.getRefreshToken() + "; HttpOnly; Secure; SameSite=Strict");
        return ResponseEntity.ok().headers(httpHeaders).build();
    }
}
