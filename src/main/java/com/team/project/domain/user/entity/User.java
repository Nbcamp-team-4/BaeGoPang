package com._team._project.domain.user.entity;

import com._team._project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_user")
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRole> userRoles = new ArrayList<>();

    public User (UUID id, String loginId, String email, String password, String name, String phone, UserStatus status) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

}