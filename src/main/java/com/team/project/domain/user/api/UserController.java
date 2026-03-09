package com.team.project.domain.user.api;

import com.team.project.domain.user.api.request.LoginUserRequest;
import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.response.AdminSignUpResponse;
import com.team.project.domain.user.api.response.ApiResponse;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/admin/signup")
    public ResponseEntity<ApiResponse<AdminSignUpResponse>> adminSignUp(@Valid @RequestBody SignUpRequest request) {
        AdminSignUpResponse response = userService.adminSignUp(request);

        return ResponseEntity.ok(
                ApiResponse.ok("회원가입이 완료되었습니다.", response)
        );
    }
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = userService.signUp(request);

        return ResponseEntity.ok(
                ApiResponse.ok("회원가입이 완료되었습니다.", response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserRequest request) {
        return userService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("로그아웃 성공");
    }

}
