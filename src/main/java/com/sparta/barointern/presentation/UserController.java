package com.sparta.barointern.presentation;

import com.sparta.barointern.presentation.dto.request.UserSignupRequestDto;
import com.sparta.barointern.presentation.dto.response.UserResponseDto;
import com.sparta.barointern.common.ApiResponse;

import com.sparta.barointern.application.UserService;
import com.sparta.barointern.application.dto.response.UserAppResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@RequestBody UserSignupRequestDto userSignupRequest) {
        UserAppResponseDto userAppResponseDto = userService.signup(userSignupRequest.toAppDto());
        return ResponseEntity.ok(ApiResponse.success(UserResponseDto.from(userAppResponseDto)));
    }


}
