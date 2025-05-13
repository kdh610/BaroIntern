package com.sparta.barointern.service;


import com.sparta.barointern.entity.User;
import com.sparta.barointern.repository.UserRepository;
import com.sparta.barointern.service.dto.request.UserSignupAppRequestDto;
import com.sparta.barointern.service.dto.response.UserAppResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserAppResponseDto signup(UserSignupAppRequestDto userSignupRequest) {
        User user = userSignupRequest.toEntity();
        userRepository.save(user);
        return UserAppResponseDto.from(user);
    }


}
