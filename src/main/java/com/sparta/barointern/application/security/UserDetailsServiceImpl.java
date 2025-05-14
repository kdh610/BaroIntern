package com.sparta.barointern.application.security;

import com.sparta.barointern.domain.entity.User;
import com.sparta.barointern.domain.repository.UserRepository;
import com.sparta.barointern.infrastructure.exception.BaseException;
import com.sparta.barointern.infrastructure.exception.Code;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[UserDetailsService] UserDetailsService loadUserByUsername");
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new BaseException(Code.USER_NOT_FOUND)
        );

        return new UserDetailsImpl(user);
    }
}
