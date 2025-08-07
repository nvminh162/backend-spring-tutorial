package com.nvminh162.projectxbackend.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nvminh162.projectxbackend.entity.User;
import com.nvminh162.projectxbackend.repository.UserRepository;
import com.nvminh162.projectxbackend.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User createUser(User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("Email already exists");
		}
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public User updateUser(Long id, User updatedUser) {
		if (userRepository.existsByEmail(updatedUser.getEmail())) {
			throw new IllegalArgumentException("Email already exists");
		}
		return userRepository.findById(id).map(user -> {
			user.setName(updatedUser.getName());
			user.setEmail(updatedUser.getEmail());
			return userRepository.save(user);
		}).orElseThrow(() -> new NoSuchElementException("User not found"));
	}

	@Override
	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new NoSuchElementException("User not found");
		}
		userRepository.deleteById(id);
	}
}
