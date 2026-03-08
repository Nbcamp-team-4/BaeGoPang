package com.team.project.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.auth.entity.OAuth;

public interface KakaoRepository extends JpaRepository<OAuth, Long> {

	Optional<OAuth> findByName(String name);

	default OAuth findUserById(Long id) {
		return this.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));
	}
}

