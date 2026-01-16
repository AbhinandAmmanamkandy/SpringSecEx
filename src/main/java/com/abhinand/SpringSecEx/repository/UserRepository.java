package com.abhinand.SpringSecEx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhinand.SpringSecEx.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
