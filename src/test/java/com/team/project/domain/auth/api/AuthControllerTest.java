package com.team.project.domain.auth.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.project.domain.auth.api.request.LoginRequest;
import com.team.project.domain.auth.api.request.SignUpRequest;
import com.team.project.domain.user.entity.Role;
import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        saveRoleIfNotExists(RoleType.ROLE_CUSTOMER);
        saveRoleIfNotExists(RoleType.ROLE_OWNER);
        saveRoleIfNotExists(RoleType.ROLE_MANAGER);
        saveRoleIfNotExists(RoleType.ROLE_ADMIN);
    }

    private void saveRoleIfNotExists(RoleType roleType) {
        if (roleRepository.findByType(roleType).isEmpty()) {
            roleRepository.save(new Role(roleType));
        }
    }

    private SignUpRequest createSignUpRequest(
            String loginId,
            String email,
            String name,
            String phone
    ) {
        return new SignUpRequest(
                loginId,
                "Test1234!",
                email,
                name,
                phone,
                RoleType.ROLE_CUSTOMER,
                "집",
                phone,
                "서울시 강남구 테헤란로",
                "101호",
                BigDecimal.valueOf(37.123456),
                BigDecimal.valueOf(127.123456),
                true
        );
    }

    private void signUpUser(
            String loginId,
            String email,
            String name,
            String phone
    ) throws Exception {
        SignUpRequest request = createSignUpRequest(loginId, email, name, phone);

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    private JsonNode loginAndGetRoot(String loginId, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(loginId, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody);
    }

    private JsonNode getLoginDataNode(JsonNode root) {
        return root.has("data") ? root.get("data") : root;
    }

    private String extractLoginId(JsonNode dataNode) {
        if (dataNode.has("loginId")) {
            return dataNode.get("loginId").asText();
        }
        if (dataNode.has("user") && dataNode.get("user").has("loginId")) {
            return dataNode.get("user").get("loginId").asText();
        }
        return null;
    }

    private String extractName(JsonNode dataNode) {
        if (dataNode.has("name")) {
            return dataNode.get("name").asText();
        }
        if (dataNode.has("user") && dataNode.get("user").has("name")) {
            return dataNode.get("user").get("name").asText();
        }
        return null;
    }

    private String extractFirstRole(JsonNode dataNode) {
        if (dataNode.has("roles") && dataNode.get("roles").isArray() && dataNode.get("roles").size() > 0) {
            return dataNode.get("roles").get(0).asText();
        }
        if (dataNode.has("user")
                && dataNode.get("user").has("roles")
                && dataNode.get("user").get("roles").isArray()
                && dataNode.get("user").get("roles").size() > 0) {
            return dataNode.get("user").get("roles").get(0).asText();
        }
        return null;
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() throws Exception {
        SignUpRequest request = new SignUpRequest(
                "dupuser",
                "Test1234!",
                "second@example.com",
                "김철수",
                "01033334444",
                RoleType.ROLE_CUSTOMER,
                "회사",
                "01033334444",
                "부산시",
                "202호",
                BigDecimal.valueOf(35.11323),
                BigDecimal.valueOf(129.1142412),
                true
        );

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."))
                .andExpect(jsonPath("$.data.loginId").value("dupuser"))
                .andExpect(jsonPath("$.data.email").value("second@example.com"))
                .andExpect(jsonPath("$.data.name").value("김철수"))
                .andExpect(jsonPath("$.data.phone").value("01033334444"))
                .andExpect(jsonPath("$.data.role").value("ROLE_CUSTOMER"));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 loginId")
    void signUp_fail_duplicateLoginId() throws Exception {
        SignUpRequest first = new SignUpRequest(
                "dupuser",
                "Test1234!",
                "first@example.com",
                "홍길동",
                "01011112222",
                RoleType.ROLE_CUSTOMER,
                "집",
                "01011112222",
                "서울시",
                "101호",
                BigDecimal.valueOf(35.11323),
                BigDecimal.valueOf(129.1142412),
                true
        );

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(first)))
                .andExpect(status().isCreated());

        SignUpRequest second = new SignUpRequest(
                "dupuser",
                "Test1234!",
                "second@example.com",
                "김철수",
                "01033334444",
                RoleType.ROLE_CUSTOMER,
                "회사",
                "01033334444",
                "부산시",
                "202호",
                BigDecimal.valueOf(35.11323),
                BigDecimal.valueOf(129.1142412),
                true
        );

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 실패 - ADMIN 권한 가입 불가")
    void signUp_fail_adminRole() throws Exception {
        SignUpRequest request = new SignUpRequest(
                "adminuser",
                "Test1234!",
                "admin@example.com",
                "관리자",
                "01099998888",
                RoleType.ROLE_ADMIN,
                "집",
                "01099998888",
                "서울시",
                "301호",
                BigDecimal.valueOf(35.11323),
                BigDecimal.valueOf(129.1142412),
                true
        );

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        signUpUser("loginuser1", "login1@example.com", "로그인유저", "01055556666");

        JsonNode root = loginAndGetRoot("loginuser1", "Test1234!");
        JsonNode dataNode = getLoginDataNode(root);

        assertNotNull(dataNode.get("accessToken"));
        assertFalse(dataNode.get("accessToken").asText().isBlank());

        assertNotNull(dataNode.get("refreshToken"));
        assertFalse(dataNode.get("refreshToken").asText().isBlank());

        assertEquals("loginuser1", extractLoginId(dataNode));
        assertEquals("로그인유저", extractName(dataNode));
        assertEquals("ROLE_CUSTOMER", extractFirstRole(dataNode));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_wrongPassword() throws Exception {
        signUpUser("loginuser2", "login2@example.com", "로그인실패유저", "01077778888");

        LoginRequest loginRequest = new LoginRequest("loginuser2", "Wrong1234!");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_success() throws Exception {
        signUpUser("reissueuser1", "reissue1@example.com", "재발급유저", "01088889999");

        JsonNode loginRoot = loginAndGetRoot("reissueuser1", "Test1234!");
        JsonNode loginData = getLoginDataNode(loginRoot);
        String refreshToken = loginData.get("refreshToken").asText();

        MvcResult result = mockMvc.perform(post("/api/auth/reissue")
                        .with(csrf())
                        .header("Refresh-Token", refreshToken))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode dataNode = getLoginDataNode(root);

        assertNotNull(dataNode.get("accessToken"));
        assertFalse(dataNode.get("accessToken").asText().isBlank());

        assertNotNull(dataNode.get("refreshToken"));
        assertFalse(dataNode.get("refreshToken").asText().isBlank());

        assertEquals("reissueuser1", extractLoginId(dataNode));
        assertEquals("재발급유저", extractName(dataNode));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() throws Exception {
        signUpUser("logoutuser1", "logout1@example.com", "로그아웃유저", "01022223333");

        JsonNode loginRoot = loginAndGetRoot("logoutuser1", "Test1234!");
        JsonNode loginData = getLoginDataNode(loginRoot);
        String accessToken = loginData.get("accessToken").asText();

        mockMvc.perform(post("/api/auth/logout")
                        .with(csrf())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그아웃 실패 - 인증 정보 없음")
    void logout_fail_unauthorized() throws Exception {
        int status = mockMvc.perform(post("/api/auth/logout")
                        .with(csrf()))
                .andReturn()
                .getResponse()
                .getStatus();

        assertTrue(status == 401 || status == 403);
    }
}