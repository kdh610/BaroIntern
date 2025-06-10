package com.sparta.barointern.domain.repository;

import com.sparta.barointern.domain.entity.User;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {

    Optional<User> findByUsername(String username);
    void save(User user);
    User grantAdmin(String username);
    List<User> findAll();
    void deleteAll();
}
