package com.team.project.domain.user.service;

import org.springframework.stereotype.Service;

import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.dto.CreateUserAddressCommand;
import com.team.project.domain.user.dto.CreateUserAddressQuery;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserAddress;
import com.team.project.domain.user.exception.UserNotFoundException;
import com.team.project.domain.user.repository.UserAddressRepository;
import com.team.project.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserAddressRepository userAddressRepository;

	@Transactional
	@Override
	public CreateUserAddressQuery createUserAdress(CreateUserAddressCommand command, UserDto userDto) {

		// 1. 사용자 객체를 찾아온다.
		User found = userRepository.findById(userDto.getId()).orElseThrow(UserNotFoundException::new);

		// 2. 주소 객체를 생성한다.
		UserAddress address = UserAddress.of(found, command.getName(), command.getPhone(), command.getAddress(),
			command.getDetailAddress(), command.getLatitude(), command.getLongitude(), false);

		// 3. 저장한다.
		UserAddress saved = userAddressRepository.save(address);

		return CreateUserAddressQuery.from(saved, userDto);
	}
}
