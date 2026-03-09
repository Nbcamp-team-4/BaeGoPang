package com.team.project.domain.user.api;

import com.team.project.domain.user.api.request.LoginUserRequest;
import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.response.ApiResponse;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = userService.signUp(request);

        return ResponseEntity.ok(
                ApiResponse.ok("회원가입이 완료되었습니다.", response)
        );
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("로그아웃 성공");
    }

}
