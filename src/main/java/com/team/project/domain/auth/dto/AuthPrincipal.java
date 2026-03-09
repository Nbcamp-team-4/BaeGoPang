package com.team.project.domain.auth.dto;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public interface AuthPrincipal {
	UUID getUserId();

	String getLoginId();

	String getName();

	Collection<? extends GrantedAuthority> getAuthorities();
}