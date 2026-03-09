package com.team.project.domain.user.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.request.UpdateUserRequest;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.dto.CreateUserAddressCommand;
import com.team.project.domain.user.dto.CreateUserAddressQuery;
import com.team.project.domain.user.entity.RoleType;

public interface UserService {

	SignUpResponse signUp(SignUpRequest request);

	public void addRole(UUID userId, RoleType roleType);

	UserResponse getUser(UUID userId);

	UserResponse updateUser(UUID userId, UpdateUserRequest request);

	void deleteUser(UUID userId);

	UserResponse getMyInfo(UserDto userDto);

	UserResponse updateMyInfo(UserDto userDto, UpdateUserRequest request);

	void deleteMyInfo(UserDto userDto);

	CreateUserAddressQuery createUserAdress(CreateUserAddressCommand command, UserDto userDto);

}
