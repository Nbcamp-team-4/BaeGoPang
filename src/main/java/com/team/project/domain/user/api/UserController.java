package com.team.project.domain.user.api;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.user.api.request.UserListRequest;
import com.team.project.domain.user.model.dto.UserList;
import com.team.project.global.common.dto.BasePageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
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
import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.request.UpdateUserRequest;
import com.team.project.domain.user.api.response.ApiResponse;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.service.UserService;
import com.team.project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users" , produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원 가입", description = "회원 가입합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원 가입 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청",content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Bad Request"
                              }
                            """
					)))})
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
		SignUpResponse response = userService.signUp(request);

		return ResponseEntity.ok(
			ApiResponse.ok("회원가입이 완료되었습니다.", response)
		);
	}

	@Operation(summary = "유저 조회", description = "관리자가 userId로 정보를 조회합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청",content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Bad Request"
                              }
                            """
					))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청",content = @Content(
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
	// 유저 정보 조회
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(userService.getUser(userId));
	}

	@Operation(summary = "유저 본인의 정보 조회", description = "사용자가 본인의 정보를 조회합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청",content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Bad Request"
                              }
                            """
					))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청",content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    Unauthorized"
                              }
                            """
					)))})
	// 본인 정보 조회
	@GetMapping
	public ResponseEntity<BaseResponse<UserResponse>> getMyInfo(@CurrentUser UserDto userDto) {
		UserResponse response = userService.getMyInfo(userDto);
		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}

	@Operation(summary = "유저 목록 조회", description = "유저 목록 데이터를 조회합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 목록 조회 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                                    {
                                     "Bad Request"
                                    }
                                    """
					)
			))
	})
	// 유저 목록 조회
	@GetMapping
	public ResponseEntity<BasePageResponse<UserList>> getUsers(UserListRequest request) {
		return ResponseEntity.ok(userService.getUsers(request));
	}

	@Operation(summary = "유저 정보 수정", description = "관리자가 유저의 정보를 수정합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 정보 수정 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청",content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Bad Request"
                              }
                            """
					))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청",content = @Content(
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
	// 유저 정보 수정
	@PatchMapping("/{userId}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
		@Valid @RequestBody UpdateUserRequest request) {
		return ResponseEntity.ok(userService.updateUser(userId, request));
	}
	@Operation(summary = "유저 본인의 정보 수정", description = "사용자가 본인의 정보를 수정합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청",content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Bad Request"
                              }
                            """
					))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청",content = @Content(
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
	// 본인 정보 수정
	@PatchMapping
	public ResponseEntity<BaseResponse<UserResponse>> updateMyInfo(@CurrentUser UserDto userDto,
		@RequestBody UpdateUserRequest request) {
		UserResponse response = userService.updateMyInfo(userDto, request);
		return ResponseEntity.ok(BaseResponse.ofSuccess(response));
	}

	@Operation(summary = "유저 탈퇴", description = "관리자가 유저를 탈퇴합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 탈퇴 성공",content =  @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Ok"
                              }
                            """
			))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청",content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Bad Request"
                              }
                            """
					))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청",content = @Content(
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
	// 유저 탈퇴
	@PatchMapping("/withdraw/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}
	@Operation(summary = "회원 탈퇴", description = "유저 본인이 탈퇴합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 탈퇴 성공",content =  @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Ok"
                              }
                            """
					))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청",content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ResponseEntity.class),
					examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
							value = """
                              {
                                    "Bad Request"
                              }
                            """
					))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청",content = @Content(
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
	// 본인 탈퇴
	@DeleteMapping
	public ResponseEntity<BaseResponse<String>> deleteMyInfo(@CurrentUser UserDto userDto) {
		userService.deleteMyInfo(userDto);
		return ResponseEntity.ok(BaseResponse.ofSuccess("회원 탈퇴가 완료되었습니다."));
	}

}
