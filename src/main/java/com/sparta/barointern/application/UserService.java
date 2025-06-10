package com.sparta.barointern.application;



import com.sparta.barointern.domain.entity.User;
import com.sparta.barointern.domain.repository.UserRepository;
import com.sparta.barointern.infrastructure.exception.BaseException;
import com.sparta.barointern.infrastructure.exception.Code;

import com.sparta.barointern.application.dto.request.UserSignupAppRequestDto;
import com.sparta.barointern.application.dto.response.UserAppResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAppResponseDto signupAdmin(UserSignupAppRequestDto userSignupRequest) {
        UserAppResponseDto signup = signup(userSignupRequest);
        return grantAdmin(signup.getUsername());
    }


    public UserAppResponseDto signup(UserSignupAppRequestDto userSignupRequest) {
        Optional<User> checkuser = userRepository.findByUsername(userSignupRequest.getUsername());
        if(checkuser.isPresent()){
            throw new BaseException(Code.USER_ALREADY_EXIST);
        }

        String password = passwordEncoder.encode(userSignupRequest.getPassword());
        User user = User.create(userSignupRequest.getUsername(), password, userSignupRequest.getNickname());


        userRepository.save(user);
        return UserAppResponseDto.from(user);
    }

    public UserAppResponseDto grantAdmin(String username){
        User user = userRepository.grantAdmin(username);
        return UserAppResponseDto.from(user);
    }

    public List<UserAppResponseDto> findAll(){
        List<User> users = userRepository.findAll();
        List<UserAppResponseDto> list = users.stream().map(UserAppResponseDto::from)
                .collect(Collectors.toList());
        return list;
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }

}
