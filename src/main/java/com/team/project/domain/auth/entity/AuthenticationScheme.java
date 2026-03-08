package com.team.project.domain.auth.entity;

import lombok.Getter;

@Getter
public enum AuthenticationScheme {

    BEARER("Bearer ");


    private final String name;

    AuthenticationScheme(String name) {
        this.name = name;
    }

    public static String generateType(AuthenticationScheme scheme) {
        return scheme.getName();
    }
}
