package com.team.project.domain.user.service;

import com.team.project.domain.auth.util.JwtProvider;
import com.team.project.domain.auth.util.UserDetailsImpl;
import com.team.project.domain.user.api.request.LoginUserRequest;
import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.response.LoginUserResponse;
import com.team.project.domain.user.api.response.SignUpResponse;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.entity.Role;
import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;
import com.team.project.domain.user.exception.CustomException;
import com.team.project.domain.user.repository.RoleRepository;
import com.team.project.domain.user.repository.UserRepository;
import com.team.project.domain.user.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        validateDuplicate(request);
        Role role = roleRepository.findByRole(request.getRole())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 권한이 존재하지 않습니다."));

        User user = new User(
                request.getLoginId(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhone()
        );

        User savedUser = userRepository.save(user);

        UserRole userRole = new UserRole(savedUser, role, request.getLoginId());
        userRoleRepository.save(userRole);

        savedUser.addUserRole(userRole);

        return SignUpResponse.from(savedUser, role.getRole());
    }


    @Override
    public ResponseEntity<?> login(LoginUserRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLoginId(),
                        request.getPassword()
                )
        );

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtProvider.createToken(
                userDetails.getUsername()
        );

        return ResponseEntity.ok(
                new LoginUserResponse(token)
        );
    }

    @Override
    @Transactional
    public UserResponse getUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserResponse(user);
    }

    @Override
    public void addRole(UUID userId, RoleType roleType) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Role role = roleRepository.findByRole(roleType)
                .orElseThrow(() -> new IllegalArgumentException("권한이 존재하지 않습니다."));

        UserRole userRole = UserRole.create(user, role);
        userRoleRepository.save(userRole);
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
