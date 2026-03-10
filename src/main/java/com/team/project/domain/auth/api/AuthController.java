package com.team.project.domain.auth.api;

import com.team.project.domain.auth.api.request.SignUpRequest;
import com.team.project.domain.user.api.response.ApiResponse;
import com.team.project.domain.auth.api.response.SignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.api.request.LoginRequest;
import com.team.project.domain.auth.api.response.LoginResponse;
import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@Tag(name = "auth", description = "auth API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "로그인", description = "로그인합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
					responseCode = "400",
					description = "잘못된 요청",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ApiResponse.class),
							examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "message": "loginId는 빈 값이 허용되지 않습니다.",
                                      "data": null
                                    }
                                    """)
					)
			),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}
	@Operation(summary = "리프래시 토큰 생성", description = "리프래시 토큰 생성합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "토큰 생성 성공"),
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
	@PostMapping("/reissue")
	public ResponseEntity<LoginResponse> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
		return ResponseEntity.ok(authService.reissue(refreshToken));
	}
	@Operation(summary = "로그아웃", description = "로그아웃합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "로그아웃 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
	})
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CurrentUser UserDto userDto) {
		authService.logout(userDto.getId());
		return ResponseEntity.noContent().build();
	}


	@Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
					responseCode = "400",
					description = "입력값 검증 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ApiResponse.class),
							examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "message": "입력값 검증에 실패했습니다.",
                                      "data": {
                                        "loginId": "loginId는 빈 값이 허용되지 않습니다."
                                      }
                                    }
                                    """)
					)
			),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "중복 데이터"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "가입 불가 권한")
	})
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
		SignUpResponse response = authService.signUp(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.ofSuccess("회원가입이 완료되었습니다.", response));
	}
}