package com.team.project.domain.user.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.dto.CreateUserAddressCommand;
import com.team.project.domain.user.dto.CreateUserAddressQuery;
import com.team.project.domain.user.dto.GetUserAddressQuery;

public interface UserAddressService {

	CreateUserAddressQuery createUserAdress(CreateUserAddressCommand command, UserDto userDto);

	GetUserAddressQuery getUserAddress(UUID userAddressId, UserDto userDto);
}
