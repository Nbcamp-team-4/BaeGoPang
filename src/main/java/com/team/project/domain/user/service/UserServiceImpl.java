package com.team.project.domain.user.service;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.api.request.AddUserRoleRequest;
import com.team.project.domain.user.api.request.UpdateUserRequest;
import com.team.project.domain.user.api.request.UserListRequest;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.entity.*;
import com.team.project.domain.user.model.dto.UserList;
import com.team.project.domain.user.repository.RoleRepository;
import com.team.project.domain.user.repository.UserRepository;
import com.team.project.domain.user.repository.UserRoleRepository;
import com.team.project.global.common.dto.BasePageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;


	@Override
	@Transactional
	public UserResponse getUser(UUID userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않거나 삭제되었습니다."));
		List<UserRole> userRoles = userRoleRepository.findByUser(user);
		List<RoleType> roles = userRoles.stream()
			.map(userRole -> userRole.getRole().getType())
			.toList();
		return UserResponse.from(user, roles);
	}

	// 본인 정보 조회
	@Override
	@Transactional
	public UserResponse getMyInfo(UserDto userDto) {
		User user = userRepository.findById(userDto.getId())
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
		List<UserRole> userRoles = userRoleRepository.findByUser(user);
		List<RoleType> roles = userRoles.stream()
			.map(userRole -> userRole.getRole().getType())
			.toList();

		return UserResponse.from(user, roles);
	}

	@Override
	@Transactional
	public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
		User user = findActiveUser(userId);

		validateUpdatableUser(user);
		validateDuplicateForUpdate(user, request);

		user.updateInfo(
			request.getEmail(),
			request.getName(),
			request.getPhone()
		);

		return UserResponse.from(user, extractRoles(user));
	}

	// 본인 정보 수정
	@Override
	@Transactional
	public UserResponse updateMyInfo(UserDto userDto, UpdateUserRequest request) {
		User user = userRepository.findById(userDto.getId())
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
		validateUpdatableUser(user);
		validateDuplicateForUpdate(user, request);

		user.updateInfo(
			request.getEmail(),
			request.getName(),
			request.getPhone()
		);
		return UserResponse.from(user, extractRoles(user));
	}

	@Override
	@Transactional
	public void deleteUser(UUID userId) {
		User user = findActiveUser(userId);

		if (user.getStatus() == UserStatus.DELETED || user.isDeleted()) {
			throw new IllegalArgumentException("이미 삭제된 유저입니다.");
		}

		user.softDelete(userId);
	}

	// 본인 탈퇴
	@Override
	public void deleteMyInfo(UserDto userDto) {
		User user = userRepository.findById(userDto.getId())
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

		if (user.getStatus() == UserStatus.DELETED || user.isDeleted()) {
			throw new IllegalArgumentException("이미 삭제된 유저입니다.");
		}

		user.softDelete(userDto.getId());
	}

	@Override
	public BasePageResponse<UserList> getUsers(UserListRequest request) {
		Pageable pageable = PageRequest.of(
				request.getPage(),
				request.getSize(),
				Sort.by(Sort.Direction.DESC, "createdAt")
		);

		Page<User> userPage = userRepository.findAllByDeletedAtIsNull(pageable);

		List<UserList> content = userPage.getContent().stream()
				.map(UserList::from)
				.toList();

		return new BasePageResponse<>(
				content,
				userPage.getNumber(),
				userPage.getSize(),
				userPage.getTotalElements(),
				userPage.getTotalPages()
		);
	}

	@Override
	public void addUserRole(UUID targetUserId, AddUserRoleRequest request, UUID currentUserId) {
		User targetUser = userRepository.findById(targetUserId)
				.orElseThrow(() -> new IllegalArgumentException("대상 유저를 찾을 수 없습니다."));

		Role role = roleRepository.findByType(request.getRoleType())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한입니다."));

		boolean alreadyExists = userRoleRepository.existsByUserAndRole(targetUser, role);
		if (alreadyExists) {
			throw new IllegalArgumentException("이미 보유한 권한입니다.");
		}

		UserRole userRole = new UserRole(targetUser, role);
		userRoleRepository.save(userRole);
	}

	private User findActiveUser(UUID userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않거나 삭제되었습니다."));
	}

	private List<RoleType> extractRoles(User user) {
		return userRoleRepository.findByUser(user).stream()
			.map(userRole -> userRole.getRole().getType())
			.toList();
	}

	private void validateUpdatableUser(User user) {
		if (user.getStatus() == UserStatus.WITHDRAWN) {
			throw new IllegalArgumentException("탈퇴한 유저는 수정할 수 없습니다.");
		}
		if (user.getStatus() == UserStatus.BLOCKED) {
			throw new IllegalArgumentException("차단된 유저는 수정할 수 없습니다.");
		}
	}
	private void validateDuplicateForUpdate(User user, UpdateUserRequest request) {
		if (request.getEmail() != null && !request.getEmail().isBlank()) {
			if (!request.getEmail().equals(user.getEmail())
					&& userRepository.existsByEmail(request.getEmail())) {
				throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
			}
		}

		if (request.getPhone() != null && !request.getPhone().isBlank()) {
			if (!request.getPhone().equals(user.getPhone())
					&& userRepository.existsByPhone(request.getPhone())) {
				throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
			}
		}
	}
}
