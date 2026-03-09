package com.team.project.domain.address.api;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.address.api.request.GetUserAllAddressRequest;
import com.team.project.domain.address.api.request.UpdateUserAddressRequest;
import com.team.project.domain.address.api.response.UserAddressResponse;
import com.team.project.global.common.dto.BasePageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/address")
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

	@GetMapping("/{addressId}")
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
	// 본인 주소 목록 조회
	@GetMapping
	public ResponseEntity<BasePageResponse<UserAddressResponse>> getMyAddresses(
			@CurrentUser UserDto userDto,
			@ModelAttribute GetUserAllAddressRequest request
	) {
		return ResponseEntity.ok(userAddressService.getMyAddresses(userDto, request));
	}

	// 본인 주소 수정
	@PatchMapping("/{addressId}")
	public ResponseEntity<UserAddressResponse> updateAddress(
			@CurrentUser UserDto userDto,
			@PathVariable UUID addressId,
			@Valid @RequestBody UpdateUserAddressRequest request
	) {
		return ResponseEntity.ok(userAddressService.updateAddress(userDto, addressId, request));
	}
	// 기본 배송지 변경
	@PatchMapping("/{addressId}/default")
	public ResponseEntity<UserAddressResponse> changeDefaultAddress(
			@CurrentUser UserDto userDto,
			@PathVariable UUID addressId
	) {
		return ResponseEntity.ok(userAddressService.changeDefaultAddress(userDto, addressId));
	}
	// 본인 주소 삭제
	@DeleteMapping("/{addressId}")
	public ResponseEntity<Void> deleteAddress(
			@CurrentUser UserDto userDto,
			@PathVariable UUID addressId
	) {
		userAddressService.deleteAddress(userDto, addressId);
		return ResponseEntity.noContent().build();
	}
}
