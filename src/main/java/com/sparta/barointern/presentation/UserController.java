package com.sparta.barointern.presentation;

import com.sparta.barointern.presentation.dto.request.UserSignupRequestDto;
import com.sparta.barointern.presentation.dto.response.UserResponseDto;
import com.sparta.barointern.common.ApiResponse;

import com.sparta.barointern.application.UserService;
import com.sparta.barointern.application.dto.response.UserAppResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@RequestBody UserSignupRequestDto userSignupRequest) {
        log.info("일반 회원가입");
        UserAppResponseDto userAppResponseDto = userService.signup(userSignupRequest.toAppDto());
        return ResponseEntity.ok(ApiResponse.success(UserResponseDto.from(userAppResponseDto)));
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<ApiResponse<UserResponseDto>> signupAdmin(@RequestBody UserSignupRequestDto userSignupRequest) {
        log.info("관리자 회원가입");
        UserAppResponseDto userAppResponseDto = userService.signupAdmin(userSignupRequest.toAppDto());
        return ResponseEntity.ok(ApiResponse.success(UserResponseDto.from(userAppResponseDto)));
    }

    @PatchMapping("/admin/users/{username}/roles")
    public ResponseEntity<UserResponseDto> grantAdmin(@PathVariable("username") String username){
        log.info("관리자부여");
        UserAppResponseDto userAppResponseDto = userService.grantAdmin(username);
        return ResponseEntity.ok(UserResponseDto.from(userAppResponseDto));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserAppResponseDto> all = userService.findAll();
        List<UserResponseDto> collect = all.stream().map(UserResponseDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }


}
