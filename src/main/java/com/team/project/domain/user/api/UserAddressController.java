package com.team.project.domain.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.api.request.CreateUserAddressRequest;
import com.team.project.domain.user.api.response.CreateUserAddressResponse;
import com.team.project.domain.user.dto.CreateUserAddressCommand;
import com.team.project.domain.user.dto.CreateUserAddressQuery;
import com.team.project.domain.user.service.UserAddressService;
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
		CreateUserAddressQuery query = userAddressService.createUserAdress(command, userDto);

		// 3. dto 변환
		CreateUserAddressResponse response = CreateUserAddressResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}
}
