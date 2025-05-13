package com.sparta.barointern.application;


import com.sparta.barointern.domain.entity.User;
import com.sparta.barointern.domain.enums.UserRole;
import com.sparta.barointern.exception.BaseException;
import com.sparta.barointern.exception.Code;
import com.sparta.barointern.infrastructure.UserRepository;
import com.sparta.barointern.application.dto.request.UserSignupAppRequestDto;
import com.sparta.barointern.application.dto.response.UserAppResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAppResponseDto signup(UserSignupAppRequestDto userSignupRequest) {
        Optional<User> checkuser = userRepository.findByUsername(userSignupRequest.getUsername());
        if(checkuser.isPresent()){
            throw new BaseException(Code.USER_ALREADY_EXIST);
        }

        String password = passwordEncoder.encode(userSignupRequest.getPassword());
        User user = User.create(userSignupRequest.getUsername(), password, userSignupRequest.getNickname(), UserRole.USER);
        userRepository.save(user);
        return UserAppResponseDto.from(user);
    }


}
