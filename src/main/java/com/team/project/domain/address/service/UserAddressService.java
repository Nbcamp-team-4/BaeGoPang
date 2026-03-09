package com.team.project.domain.address.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.address.api.request.GetUserAllAddressRequest;
import com.team.project.domain.address.api.request.UpdateUserAddressRequest;
import com.team.project.domain.address.api.response.UserAddressResponse;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.address.dto.CreateUserAddressCommand;
import com.team.project.domain.address.dto.CreateUserAddressQuery;
import com.team.project.domain.address.dto.GetUserAddressQuery;
import com.team.project.global.common.dto.BasePageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAddressService {

	CreateUserAddressQuery createUserAddress(CreateUserAddressCommand command, UserDto userDto);

	GetUserAddressQuery getUserAddress(UUID userAddressId, UserDto userDto);

	BasePageResponse<UserAddressResponse> getMyAddresses(UserDto userDto, GetUserAllAddressRequest request);

	UserAddressResponse updateAddress(UserDto userDto, UUID addressId, UpdateUserAddressRequest request);

	void deleteAddress(UserDto userDto, UUID addressId);
}
