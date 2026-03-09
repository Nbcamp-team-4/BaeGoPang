package com.team.project.domain.auth.dto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.team.project.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomUserPrincipal implements UserDetails, AuthPrincipal {

	private UUID userId;
	private String loginId;
	private String password;
	private String name;
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public String getUsername() {
		return loginId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static CustomUserPrincipal from(User user) {

		List<GrantedAuthority> authorities = user.getUserRoles().stream()
			.map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getType().name()))
			.collect(Collectors.toList());

		return CustomUserPrincipal.builder()
			.userId(user.getId())
			.loginId(user.getLoginId())
			.password(user.getPassword())
			.name(user.getName())
			.authorities(authorities)
			.build();
	}
}