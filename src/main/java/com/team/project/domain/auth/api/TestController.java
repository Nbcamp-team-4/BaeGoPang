package com.team.project.domain.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.global.common.dto.BaseResponse;

@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping
	public ResponseEntity<BaseResponse<?>> test(@CurrentUser UserDto userDto) {
		return ResponseEntity.ok().body(BaseResponse.ofSuccess(userDto));
	}

	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@GetMapping("/customer")
	public ResponseEntity<BaseResponse<?>> testUser(@CurrentUser UserDto userDto) {
		return ResponseEntity.ok().body(BaseResponse.ofSuccess(userDto));
	}

	@PreAuthorize("hasAnyRole('OWNER')")
	@GetMapping("/owner")
	public ResponseEntity<BaseResponse<?>> testOwner(@CurrentUser UserDto userDto) {
		return ResponseEntity.ok().body(BaseResponse.ofSuccess(userDto));
	}
}
