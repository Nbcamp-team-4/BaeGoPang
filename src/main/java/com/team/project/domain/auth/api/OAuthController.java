package com._team._project.domain.auth.api;

import com._team._project.domain.auth.api.request.KakaoLogin;
import com._team._project.domain.auth.api.request.KakaoToken;
import com._team._project.domain.auth.api.response.JwtAuthResponse;
import com._team._project.domain.auth.service.OAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@AllArgsConstructor
public class OAuthController {

    private OAuthService oAuthService;

    @ResponseBody
    @GetMapping("/auth/kakao/LoginHandler")
    public ResponseEntity<CommonResponseBody<?>> kakaoCallback(@RequestParam String code) {

        KakaoToken getToken = oAuthService.getKakaoAccessToken(code);

        if (getToken == null || getToken.getAccess_token() == null) {
            log.error("카카오 Access Token 발급 실패: {}", getToken);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponseBody<>("카카오 토큰 발급 실패", null));
        }

        String accessToken = getToken.getAccess_token();
        HashMap<String, Object> userInfo = oAuthService.getKakaoUserInfo(accessToken);
        String nickname = userInfo.get("nickname").toString();
        Long id = Long.parseLong(userInfo.get("id").toString());
        KakaoLogin loginDto = new KakaoLogin(nickname, id);
        JwtAuthResponse dto = oAuthService.login(loginDto);

        return ResponseEntity.ok(new CommonResponseBody<>("login success", dto));
    }
}
