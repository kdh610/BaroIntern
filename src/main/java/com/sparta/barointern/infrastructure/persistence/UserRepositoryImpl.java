package com.sparta.barointern.infrastructure.persistence;

import com.sparta.barointern.domain.entity.User;
import com.sparta.barointern.domain.repository.UserRepository;
import com.sparta.barointern.infrastructure.exception.BaseException;
import com.sparta.barointern.infrastructure.exception.Code;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static Map<String,User> store = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findByUsername(String username) {
       return Optional.ofNullable(store.get(username));
    }

    @Override
    public void save(User user) {
        store.put(user.getUsername(),user);
    }

    @Override
    public User grantAdmin(String username) {
        User finduser = findByUsername(username).orElseThrow(() -> new BaseException(Code.USER_NOT_FOUND));
        finduser.grantAdmin();
        store.replace(username,finduser);
        return finduser;
    }

    @Override
    public List<User> findAll() {
        return store.values().stream().collect(Collectors.toList());
    }
}
