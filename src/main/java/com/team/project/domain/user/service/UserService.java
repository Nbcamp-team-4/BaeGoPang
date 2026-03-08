package com.team.project.domain.user.service;

import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.entity.RoleType;

import java.util.UUID;

public interface UserService {
    void signUp(SignUpRequest request);

    UserResponse getUser(UUID userId);

    void addRole(UUID userId, RoleType roleType);
}
