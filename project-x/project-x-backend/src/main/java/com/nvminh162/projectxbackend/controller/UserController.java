package com.nvminh162.projectxbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nvminh162.projectxbackend.entity.ApiResponse;
import com.nvminh162.projectxbackend.entity.User;
import com.nvminh162.projectxbackend.service.impl.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
public class UserController {

	private final UserServiceImpl userService;

	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@PostMapping("/users")
	public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
		User created = userService.createUser(user);
		var result = new ApiResponse<>(HttpStatus.CREATED, "Created User", created, null);
		// ApiResponse<User> result = new ApiResponse<User>(HttpStatus.CREATED, "Created
		// User", created, null);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	@GetMapping("/users")
	public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
		var result = new ApiResponse<List<User>>(HttpStatus.OK, "Get All Users", userService.getAllUsers(), null);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
		return userService.getUserById(id).map(user -> {
			var result = new ApiResponse<>(HttpStatus.OK, "Get User By ID", user, null);
			return ResponseEntity.ok(result);
		}).orElseGet(() -> {
			ApiResponse<User> errorRes = new ApiResponse<User>(HttpStatus.NOT_FOUND,
					"Không tìm thấy User với ID là: " + id, null, "USER_NOT_FOUND");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRes);
		});
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
		User updated = userService.updateUser(id, user);
		var result = new ApiResponse<>(HttpStatus.OK, "Update User", updated, null);
		return ResponseEntity.ok(result);
	}

	/*
	 * @ExceptionHandler(NoSuchElementException.class)
	 * public ResponseEntity<?> handleNotFound(NoSuchElementException ex) {
	 * var result = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
	 * "Handle Not Found", null, ex.getMessage());
	 * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	 * }
	 */
	@DeleteMapping("/users/{id}")
	public ResponseEntity<ApiResponse<User>> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		ApiResponse<User> result = new ApiResponse<>(HttpStatus.NO_CONTENT, "Delete User", null, null);
		return ResponseEntity.ok(result);
	}
}
