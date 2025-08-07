package com.nvminh162.projectxbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nvminh162.projectxbackend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
