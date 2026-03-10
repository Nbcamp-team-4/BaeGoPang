package com.team.project.domain.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.auth.dto.CustomUserPrincipal;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.exception.UserNotFoundException;
import com.team.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername loginId = [" + loginId + "]");

		User user = userRepository.findByLoginId(loginId)
			.orElseThrow(() -> new UserNotFoundException());

		return CustomUserPrincipal.from(user);
	}
}