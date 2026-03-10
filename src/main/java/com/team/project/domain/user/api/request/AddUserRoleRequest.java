package com.team.project.domain.user.api.request;

import com.team.project.domain.user.entity.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRoleRequest {

    @NotNull(message = "추가할 권한은 필수입니다.")
    private RoleType roleType;
}
