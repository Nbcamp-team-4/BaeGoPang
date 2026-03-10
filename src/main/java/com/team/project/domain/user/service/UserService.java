package com.team.project.domain.user.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.api.request.AddUserRoleRequest;
import com.team.project.domain.user.api.request.UpdateUserRequest;
import com.team.project.domain.user.api.request.UserListRequest;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.model.dto.UserList;
import com.team.project.global.common.dto.BasePageResponse;
import jakarta.validation.Valid;

public interface UserService {


	UserResponse getUser(UUID userId);

	UserResponse updateUser(UUID userId, UpdateUserRequest request);

	void deleteUser(UUID userId);

	UserResponse getMyInfo(UserDto userDto);

	UserResponse updateMyInfo(UserDto userDto, UpdateUserRequest request);

	void deleteMyInfo(UserDto userDto);

	BasePageResponse<UserList> getUsers(UserListRequest request);

	void addUserRole(UUID userId, @Valid AddUserRoleRequest request, UUID currentUserId);
}
