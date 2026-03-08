package com.team.project.domain.user.api;

import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ResponseEntity;
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
    public ResponseEntity<Void> signup(@RequestBody SignUpRequest request) {

        userService.signUp(request);

        return ResponseEntity.ok().build();
    }

}
