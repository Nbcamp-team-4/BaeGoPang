package com.team.project.domain.user.service;

import com.team.project.domain.user.api.request.LoginUserRequest;
import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.entity.RoleType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserService {

    AdminSignUpResponse adminSignUp(AdminSignUpRequest request);

    SignUpResponse signUp(SignUpRequest request);

    ResponseEntity<?> login(LoginUserRequest request);

    UserResponse getUser(UUID userId);

    void addRole(UUID userId, RoleType roleType);
}