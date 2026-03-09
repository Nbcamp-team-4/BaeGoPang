package com.team.project.domain.user.service;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.api.request.*;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.entity.RoleType;

import java.util.List;
import java.util.UUID;

public interface UserService {

    SignUpResponse signUp(SignUpRequest request);

    public void addRole(UUID userId, RoleType roleType);

    UserResponse getUser(UUID userId);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    void deleteUser(UUID userId);

    UserResponse getMyInfo(UserDto userDto);

    UserResponse updateMyInfo(UserDto userDto, UpdateUserRequest request);

    void deleteMyInfo(UserDto userDto);
}