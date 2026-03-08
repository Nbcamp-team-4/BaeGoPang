package com._team._project.domain.user.entity;

import com._team._project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_role")
@Getter
@NoArgsConstructor
public class Role extends BaseEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;
}
