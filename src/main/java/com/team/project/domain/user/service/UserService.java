package com.team.project.domain.user.service;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.dto.CreateUserAddressCommand;
import com.team.project.domain.user.dto.CreateUserAddressQuery;

public interface UserService {
	CreateUserAddressQuery createUserAdress(CreateUserAddressCommand command, UserDto userDto);
}
