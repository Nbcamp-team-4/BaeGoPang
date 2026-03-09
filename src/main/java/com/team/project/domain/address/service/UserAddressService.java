package com.team.project.domain.address.service;

import java.util.UUID;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.address.dto.CreateUserAddressCommand;
import com.team.project.domain.address.dto.CreateUserAddressQuery;
import com.team.project.domain.address.dto.GetUserAddressQuery;

public interface UserAddressService {

	CreateUserAddressQuery createUserAddress(CreateUserAddressCommand command, UserDto userDto);

	GetUserAddressQuery getUserAddress(UUID userAddressId, UserDto userDto);
}
