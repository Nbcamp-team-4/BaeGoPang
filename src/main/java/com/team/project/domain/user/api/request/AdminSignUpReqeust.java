package com.team.project.domain.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AdminSignUpReqeust {

    @NotBlank(message = "loginId는 빈 값이 허용되지 않습니다.")
    private final String loginId;

    @NotBlank(message = "password는 빈 값이 허용되지 않습니다.")
    private final String password;

    @NotBlank(message = "email은 빈 값이 허용되지 않습니다.")
//    @Email(message = "올바른 email 형식이 아닙니다.")
    private final String email;

    @NotBlank(message = "name은 빈 값이 허용되지 않습니다.")
    private final String name;

    @NotBlank(message = "phone은 빈 값이 허용되지 않습니다.")
    private final String phone;

    public AdminSignUpRequest(String loginId, String password, String email, String name, String phone) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

}
