package com.team.project.domain.user.service;

import com.team.project.domain.auth.util.JwtProvider;
import com.team.project.domain.auth.util.UserDetailsImpl;
import com.team.project.domain.user.api.request.LoginUserRequest;
import com.team.project.domain.user.api.request.SignUpRequest;
import com.team.project.domain.user.api.response.LoginUserResponse;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.entity.Role;
import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;
import com.team.project.domain.user.repository.RoleRepository;
import com.team.project.domain.user.repository.UserRepository;
import com.team.project.domain.user.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> signUp(SignUpRequest request) {

        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 로그인 ID 입니다.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        // 사용자 생성
        User user = User.builder()
                .loginId(request.getLoginId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .status(request.getStatus())
                .build();

        userRepository.save(user);

        // 기본 권한 CUSTOMER 부여
        Role role = roleRepository.findByRole(RoleType.CUSTOMER)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        UserRole userRole = UserRole.create(user, role);
        userRoleRepository.save(userRole);

        return ResponseEntity.ok("회원가입 성공");
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
}
