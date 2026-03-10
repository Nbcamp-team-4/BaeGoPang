package com.team.project.domain.address.api;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.address.api.request.CreateUserAddressRequest;
import com.team.project.domain.address.api.request.GetUserAllAddressRequest;
import com.team.project.domain.address.api.request.UpdateUserAddressRequest;
import com.team.project.domain.address.api.response.CreateUserAddressResponse;
import com.team.project.domain.address.api.response.GetUserAddressResponse;
import com.team.project.domain.address.api.response.UserAddressResponse;
import com.team.project.domain.address.dto.CreateUserAddressCommand;
import com.team.project.domain.address.dto.CreateUserAddressQuery;
import com.team.project.domain.address.dto.GetUserAddressQuery;
import com.team.project.domain.address.service.UserAddressService;
import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.global.common.dto.BasePageResponse;
import com.team.project.global.common.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Address", description = "배송지 API")
@RestController
@RequestMapping(value = "/api/address", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserAddressController {

	private final UserAddressService userAddressService;

	@Operation(summary = "배송지 등록", description = "배송지를 등록합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원 가입 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Bad Request"
					  }
					"""
			)))})
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

	@Operation(summary = "배송지 조회", description = "배송지Id로 배송지 정보를 조회합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Bad Request"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        Unauthorized"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 정보 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Not Found"
					  }
					"""
			)
		))
	})
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

	@Operation(summary = "본인 배송지 조회", description = "본인의 배송지 정보를 조회합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Bad Request"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        Unauthorized"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 정보 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Not Found"
					  }
					"""
			)
		))
	})
	@GetMapping
	public ResponseEntity<BasePageResponse<UserAddressResponse>> getMyAddresses(
		@CurrentUser UserDto userDto,
		@ModelAttribute GetUserAllAddressRequest request
	) {
		return ResponseEntity.ok(userAddressService.getMyAddresses(userDto, request));
	}

	@Operation(summary = "배송지 수정", description = "배송지Id로 배송지 정보를 수정합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Bad Request"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        Unauthorized"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 정보 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Not Found"
					  }
					"""
			)
		))
	})
	// 본인 주소 수정
	@PatchMapping("/{addressId}")
	public ResponseEntity<UserAddressResponse> updateAddress(
		@CurrentUser UserDto userDto,
		@PathVariable UUID addressId,
		@Valid @RequestBody UpdateUserAddressRequest request
	) {
		return ResponseEntity.ok(userAddressService.updateAddress(userDto, addressId, request));
	}

	@Operation(summary = "기본 배송지 변경", description = "기본 배송지를 변경합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Bad Request"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        Unauthorized"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 정보 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Not Found"
					  }
					"""
			)
		))
	})
	// 기본 배송지 변경
	@PatchMapping("/{addressId}/default")
	public ResponseEntity<UserAddressResponse> changeDefaultAddress(
		@CurrentUser UserDto userDto,
		@PathVariable UUID addressId
	) {
		return ResponseEntity.ok(userAddressService.changeDefaultAddress(userDto, addressId));
	}

	@Operation(summary = "배송지 삭제", description = "배송지ID로 배송지를 삭제합니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "배송지 삭제 성공", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Ok"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Bad Request"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        Unauthorized"
					  }
					"""
			))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 정보 찾을 수 없음", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ResponseEntity.class),
			examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
				value = """
					  {
					        "Not Found"
					  }
					"""
			)
		))
	})
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
