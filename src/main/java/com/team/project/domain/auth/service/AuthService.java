package com.team.project.domain.auth.service;

import java.util.List;
import java.util.UUID;

import com.team.project.domain.address.dto.CreateUserAddressCommand;
import com.team.project.domain.address.dto.CreateUserAddressQuery;
import com.team.project.domain.address.service.UserAddressService;
import com.team.project.domain.auth.api.request.SignUpRequest;
import com.team.project.domain.auth.api.response.SignUpResponse;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.exception.CustomException;
import com.team.project.domain.user.repository.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.team.project.domain.auth.api.request.LoginRequest;
import com.team.project.domain.auth.api.response.LoginResponse;
import com.team.project.domain.auth.dto.CustomUserPrincipal;
import com.team.project.domain.user.entity.Role;
import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;
import com.team.project.domain.user.exception.UserNotFoundException;
import com.team.project.domain.user.repository.RoleRepository;
import com.team.project.domain.user.repository.UserRepository;
import com.team.project.global.jwt.JwtTokenProvider;
import com.team.project.global.jwt.exception.InvalidTokenException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRoleRepository userRoleRepository;
	private final UserAddressService userAddressService;

	public LoginResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.getLoginId(),
				request.getPassword()
			)
		);

		CustomUserPrincipal principal = (CustomUserPrincipal)authentication.getPrincipal();

		String accessToken = jwtTokenProvider.createAccessToken(principal);
		String refreshToken = jwtTokenProvider.createRefreshToken(principal);

		User user = userRepository.findById(principal.getUserId())
			.orElseThrow(() -> new UserNotFoundException());
		user.updateRefreshToken(refreshToken);

		List<RoleType> types = user.getUserRoles().stream().map(r -> r.getRole().getType()).toList();

		return new LoginResponse(accessToken, refreshToken, user.getLoginId(), user.getName(), types);
	}

	public LoginResponse reissue(String refreshToken) {
		if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
			throw new InvalidTokenException(); // 토큰이 유효하지 않은 경우
		}

		DecodedJWT decodedJWT = jwtTokenProvider.verify(refreshToken);
		UUID userId = UUID.fromString(decodedJWT.getClaim("userId").asString());

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException());

		if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
			throw new InvalidTokenException(); // 저장된 토큰과 불일치
		}

		CustomUserPrincipal principal = CustomUserPrincipal.from(user);

		String newAccessToken = jwtTokenProvider.createAccessToken(principal);
		String newRefreshToken = jwtTokenProvider.createRefreshToken(principal);

		user.updateRefreshToken(newRefreshToken);

		List<RoleType> roleTypes = user.getUserRoles().stream().map(r -> r.getRole().getType()).toList();

		return new LoginResponse(newAccessToken, newRefreshToken, user.getLoginId(), user.getName(), roleTypes);
	}

	public void logout(UUID userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException());
		user.clearRefreshToken();
	}

	public SignUpResponse signUp(SignUpRequest request) {

		validateDuplicate(request);
		validateSignUpRole(request.getRole());

		// role 조회
		Role role = roleRepository.findByType(request.getRole())
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 권한이 존재하지 않습니다."));

		// 유저 생성
		User user = new User(
				request.getLoginId(),
				request.getEmail(),
				passwordEncoder.encode(request.getPassword()),
				request.getName(),
				request.getPhone()
		);
		User savedUser = userRepository.save(user);
		// 권한 연결
		UserRole userRole = new UserRole(savedUser, role);
		userRoleRepository.save(userRole);
		savedUser.addUserRole(userRole);
		// 주소 command 생성
		CreateUserAddressCommand command = CreateUserAddressCommand.of(
				request.getAddressName(),
				request.getAddressPhone(),
				request.getAddress(),
				request.getDetailAddress(),
				request.getLatitude(),
				request.getLongitude(),
				request.getIsDefault()
		);
		// User → UserDto 변환
		UserDto userDto = UserDto.from(savedUser);
		// 주소 생성
		CreateUserAddressQuery addressQuery = userAddressService.createUserAddress(command, userDto);
		//응답 반환
		return SignUpResponse.from(savedUser, role.getType(), addressQuery);

	}
	private void validateSignUpRole(RoleType roleType) {
		if (roleType == RoleType.ROLE_ADMIN) {
			throw new CustomException(HttpStatus.FORBIDDEN, "ADMIN 권한으로는 회원가입할 수 없습니다.");
		}
	}
	private void validateDuplicate(SignUpRequest request) {
		if (userRepository.existsByLoginId(request.getLoginId())) {
			throw new CustomException(HttpStatus.CONFLICT, "이미 사용 중인 loginId 입니다.");
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new CustomException(HttpStatus.CONFLICT, "이미 사용 중인 email 입니다.");
		}

		if (userRepository.existsByPhone(request.getPhone())) {
			throw new CustomException(HttpStatus.CONFLICT, "이미 사용 중인 phone 입니다.");
		}
	}

}