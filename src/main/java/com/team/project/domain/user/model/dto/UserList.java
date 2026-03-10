package com.team.project.domain.user.model.dto;

import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserList {

    private final UUID id;
    private final String loginId;
    private final String email;
    private final String name;
    private final String phone;
    private final List<RoleType> roles;

    public static UserList from(User user) {
        List<RoleType> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getType())
                .toList();

        return new UserList(
                user.getId(),
                user.getLoginId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                roles
        );
    }
}
