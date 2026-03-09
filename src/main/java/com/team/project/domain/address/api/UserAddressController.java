package com.team.project.domain.address.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.address.api.request.CreateUserAddressRequest;
import com.team.project.domain.address.api.response.CreateUserAddressResponse;
import com.team.project.domain.address.api.response.GetUserAddressResponse;
import com.team.project.domain.address.dto.CreateUserAddressCommand;
import com.team.project.domain.address.dto.CreateUserAddressQuery;
import com.team.project.domain.address.dto.GetUserAddressQuery;
import com.team.project.domain.address.service.UserAddressService;
import com.team.project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-address")
@RequiredArgsConstructor
public class UserAddressController {

	private final UserAddressService userAddressService;

	@PostMapping
	public ResponseEntity<BaseResponse<CreateUserAddressResponse>> createUserAdress(
		@RequestBody @Valid CreateUserAddressRequest request, @CurrentUser UserDto userDto) {

		// 1. command 객체로 변환
		CreateUserAddressCommand command = CreateUserAddressCommand.of(request.getName(),
			request.getPhone(), request.getAddress(), request.getDetailAddress(), request.getLatitude(),
			request.getLongitude(), request.getIsDefault());

		// 2. service 호출
		CreateUserAddressQuery query = userAddressService.createUserAddress(command, userDto);

		// 3. dto 변환
		CreateUserAddressResponse response = CreateUserAddressResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

	@GetMapping("/{userAddressId}")
	public ResponseEntity<BaseResponse<GetUserAddressResponse>> getUserAddress(
		@PathVariable("userAddressId") UUID userAddressId, @CurrentUser UserDto userDto) {

		// 1. service 호출
		GetUserAddressQuery query = userAddressService.getUserAddress(userAddressId, userDto);

		// 2. dto 변환
		GetUserAddressResponse response = GetUserAddressResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}
}
