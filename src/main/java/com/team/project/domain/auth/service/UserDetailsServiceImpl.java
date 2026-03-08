package com.team.project.domain.auth.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.team.project.domain.auth.exception.UserRoleNotFoundException;
import com.team.project.domain.auth.util.UserDetailsImpl;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;
import com.team.project.domain.user.repository.UserRepository;
import com.team.project.domain.user.repository.UserRoleRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;

	public UserDetailsServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		User user = userRepository.findByLoginId(loginId)
			.orElseThrow(() -> new UsernameNotFoundException("Not Found " + loginId));

		List<UserRole> userRoles = userRoleRepository.findByUser(user);
		if (userRoles.isEmpty()) {
			throw new UserRoleNotFoundException();
		}

		return new UserDetailsImpl(user, userRoles);
	}
}