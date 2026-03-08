package com.team.project.domain.auth.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;

public class UserDetailsImpl implements UserDetails {

	private final User user;
	private final List<UserRole> userRoles;

	public UserDetailsImpl(User user, List<UserRole> userRoles) {
		this.user = user;
		this.userRoles = userRoles;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getLoginId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userRoles.stream()
			.map(userRole -> userRole.getRole().getRole())
			.map(RoleType::getAuthority)
			.distinct()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
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
}
