package com.team.project.domain.auth.service;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.team.project.domain.address.dto.CreateUserAddressCommand;
import com.team.project.domain.address.dto.CreateUserAddressQuery;
import com.team.project.domain.address.service.UserAddressService;
import com.team.project.domain.auth.api.request.LoginRequest;
import com.team.project.domain.auth.api.request.SignUpRequest;
import com.team.project.domain.auth.api.response.LoginResponse;
import com.team.project.domain.auth.api.response.SignUpResponse;
import com.team.project.domain.auth.dto.CustomUserPrincipal;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.entity.Role;
import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;
import com.team.project.domain.user.exception.CustomException;
import com.team.project.domain.user.repository.RoleRepository;
import com.team.project.domain.user.repository.UserRepository;
import com.team.project.domain.user.repository.UserRoleRepository;
import com.team.project.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserAddressService userAddressService;

    @Mock
    private Authentication authentication;

    @Mock
    private DecodedJWT decodedJWT;

    @Mock
    private Claim claim;

    private User user;
    private Role customerRole;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        customerRole = new Role(RoleType.ROLE_CUSTOMER);

        user = new User(
                "testuser",
                "test@example.com",
                "encodedPassword",
                "홍길동",
                "01012345678"
        );

        UserRole userRole = new UserRole(user, customerRole);
        user.addUserRole(userRole);
        user.updateRefreshToken("valid-refresh-token");
    }

    private SignUpRequest createSignUpRequest(String loginId, String email, String phone, RoleType roleType) {
        return new SignUpRequest(
                loginId,
                "Test1234!",
                email,
                "홍길동",
                phone,
                roleType,
                "집",
                phone,
                "서울시 강남구",
                "101호",
                BigDecimal.valueOf(37.123456),
                BigDecimal.valueOf(127.123456),
                true
        );
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        SignUpRequest request = createSignUpRequest(
                "testuser",
                "test@example.com",
                "01012345678",
                RoleType.ROLE_CUSTOMER
        );

        CreateUserAddressQuery addressQuery = mock(CreateUserAddressQuery.class);

        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(request.getPhone())).thenReturn(false);
        when(roleRepository.findByType(RoleType.ROLE_CUSTOMER)).thenReturn(Optional.of(customerRole));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRoleRepository.save(any(UserRole.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userAddressService.createUserAddress(any(CreateUserAddressCommand.class), any(UserDto.class)))
                .thenReturn(addressQuery);

        SignUpResponse response = authService.signUp(request);

        assertNotNull(response);
        assertEquals("testuser", response.getLoginId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("홍길동", response.getName());
        assertEquals("01012345678", response.getPhone());
        assertEquals(RoleType.ROLE_CUSTOMER, response.getRole());

        verify(userRepository).save(any(User.class));
        verify(userRoleRepository).save(any(UserRole.class));
        verify(userAddressService).createUserAddress(any(CreateUserAddressCommand.class), any(UserDto.class));
    }

    @Test
    @DisplayName("회원가입 실패 - loginId 중복")
    void signUp_fail_duplicateLoginId() {
        SignUpRequest request = createSignUpRequest(
                "testuser",
                "test@example.com",
                "01012345678",
                RoleType.ROLE_CUSTOMER
        );

        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> authService.signUp(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    @DisplayName("회원가입 실패 - ADMIN 권한 가입 불가")
    void signUp_fail_adminRole() {
        SignUpRequest request = createSignUpRequest(
                "adminuser",
                "admin@example.com",
                "01099998888",
                RoleType.ROLE_ADMIN
        );

        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(request.getPhone())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authService.signUp(request));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        LoginRequest request = new LoginRequest("testuser", "Test1234!");
        CustomUserPrincipal principal = mock(CustomUserPrincipal.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getUserId()).thenReturn(userId);

        when(jwtTokenProvider.createAccessToken(principal)).thenReturn("access-token");
        when(jwtTokenProvider.createRefreshToken(principal)).thenReturn("refresh-token");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        assertNotNull(response.getUser());
        assertEquals("testuser", response.getUser().getLoginId());
        assertEquals("홍길동", response.getUser().getName());
        assertEquals(List.of(RoleType.ROLE_CUSTOMER), response.getUser().getRoles());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_success() {
        String refreshToken = "valid-refresh-token";
        CustomUserPrincipal principal = mock(CustomUserPrincipal.class);

        when(jwtTokenProvider.isRefreshToken(refreshToken)).thenReturn(true);
        when(jwtTokenProvider.verify(refreshToken)).thenReturn(decodedJWT);
        when(decodedJWT.getClaim("userId")).thenReturn(claim);
        when(claim.asString()).thenReturn(userId.toString());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        try (MockedStatic<CustomUserPrincipal> mockedStatic = mockStatic(CustomUserPrincipal.class)) {
            mockedStatic.when(() -> CustomUserPrincipal.from(user)).thenReturn(principal);

            when(jwtTokenProvider.createAccessToken(principal)).thenReturn("new-access-token");
            when(jwtTokenProvider.createRefreshToken(principal)).thenReturn("new-refresh-token");

            LoginResponse response = authService.reissue(refreshToken);

            assertNotNull(response);
            assertEquals("new-access-token", response.getAccessToken());
            assertEquals("new-refresh-token", response.getRefreshToken());

            assertNotNull(response.getUser());
            assertEquals("testuser", response.getUser().getLoginId());
            assertEquals("홍길동", response.getUser().getName());
            assertEquals(List.of(RoleType.ROLE_CUSTOMER), response.getUser().getRoles());

            verify(jwtTokenProvider).isRefreshToken(refreshToken);
            verify(jwtTokenProvider).verify(refreshToken);
            verify(userRepository).findById(userId);
        }
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> authService.logout(userId));

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("로그아웃 실패 - 유저 없음")
    void logout_fail_userNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CustomException exception =
                assertThrows(CustomException.class, () -> authService.logout(userId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}