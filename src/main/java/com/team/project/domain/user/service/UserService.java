package com.team.project.domain.user.service;

import com.team.project.domain.user.api.request.*;
import com.team.project.domain.user.api.request.LoginUserRequest;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.api.response.UserResponse;

import java.util.UUID;

public interface UserService {

    SignUpResponse signUp(SignUpRequest request);

    UserResponse getUser(UUID userId);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    UserResponse updateUserStatus(UUID userId, UpdateStatusRequest request);

    void deleteUser(UUID actorId, UUID targetUserId);
}