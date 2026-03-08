package com._team._project.domain.auth.service;

import com._team._project.domain.auth.util.UserDetailsImpl;
import com._team._project.domain.user.entity.User;
import com._team._project.domain.user.entity.UserRole;
import com._team._project.domain.user.repository.UserRepository;
import com._team._project.domain.user.repository.UserRoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        UserRole userRole = userRoleRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + loginId));

        return new UserDetailsImpl(user);
    }
}