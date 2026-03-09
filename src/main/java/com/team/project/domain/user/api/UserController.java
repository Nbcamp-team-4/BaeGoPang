package com.team.project.domain.user.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.api.request.CreateUserAddressRequest;
import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.request.UpdateUserRequest;
import com.team.project.domain.user.api.response.ApiResponse;
import com.team.project.domain.user.api.response.CreateUserAddressResponse;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.dto.CreateUserAddressCommand;
import com.team.project.domain.user.dto.CreateUserAddressQuery;
import com.team.project.domain.user.service.UserService;
import com.team.project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	@PostMapping("/user-address")
	public ResponseEntity<BaseResponse<CreateUserAddressResponse>> createUserAdress(
		@RequestBody @Valid CreateUserAddressRequest request, @CurrentUser UserDto userDto) {

		// 1. command 객체로 변환
		CreateUserAddressCommand command = CreateUserAddressCommand.of(request.getName(),
			request.getPhone(), request.getAddress(), request.getDetailAddress(), request.getLatitude(),
			request.getLongitude());

		// 2. service 호출
		CreateUserAddressQuery query = userService.createUserAdress(command, userDto);

		// 3. dto 변환
		CreateUserAddressResponse response = CreateUserAddressResponse.from(query);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
		SignUpResponse response = userService.signUp(request);

		return ResponseEntity.ok(
			ApiResponse.ok("회원가입이 완료되었습니다.", response)
		);
	}

	// 유저 정보 조회
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(userService.getUser(userId));
	}

	// 본인 정보 조회
	@GetMapping
	public ResponseEntity<BaseResponse<UserResponse>> getMyInfo(@CurrentUser UserDto userDto) {
		UserResponse response = userService.getMyInfo(userDto);
		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}

	// 유저 정보 수정
	@PatchMapping("/{userId}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
		@Valid @RequestBody UpdateUserRequest request) {
		return ResponseEntity.ok(userService.updateUser(userId, request));
	}

	// 본인 정보 수정
	@PatchMapping
	public ResponseEntity<BaseResponse<UserResponse>> updateMyInfo(@CurrentUser UserDto userDto,
		@RequestBody UpdateUserRequest request) {
		UserResponse response = userService.updateMyInfo(userDto, request);
		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}

	// 유저 탈퇴
	@PatchMapping("/withdraw/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

	// 본인 탈퇴
	@DeleteMapping
	public ResponseEntity<BaseResponse<String>> deleteMyInfo(@CurrentUser UserDto userDto) {
		userService.deleteMyInfo(userDto);
		return ResponseEntity.ok(BaseResponse.ofSuccess("회원 탈퇴가 완료되었습니다."));
	}

}
