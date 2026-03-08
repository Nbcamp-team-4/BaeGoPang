package com.team.project.domain.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.team.project.domain.auth.util.UserDetailsImpl;
import com.team.project.domain.user.api.response.UserResponse;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Security::UserService")
public class UserService {

	public UserResponse doSomethingAsUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		User user = userDetails.getUser();
		UserRole role = userDetails.getRole();

		log.info("USER 로직 실행.");

		return new UserResponse(
			user.getId(),
			user.getEmail(),
			user.getRole().getName()
		);
	}
}
