package com.environment.environment.repository;

import com.environment.environment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findUserById(Long id);
    public User getUserById(Long userId);
    Optional<User> findByNickname(String nickname);
}
