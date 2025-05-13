package com.sparta.barointern.repository;

import com.sparta.barointern.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
