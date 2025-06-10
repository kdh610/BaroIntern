package com.sparta.barointern.presentation;

import com.sparta.barointern.application.UserService;
import com.sparta.barointern.application.dto.response.UserAppResponseDto;
import com.sparta.barointern.common.ApiResponse;
import com.sparta.barointern.presentation.dto.request.UserSignupRequestDto;
import com.sparta.barointern.presentation.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "사용자 관리 API", description = "일반 유저 / 관리자 회원가입 & 관리자권한 부여 & 모든 사용자 정보 조회 기능 ")
public class UserController {

    private final UserService userService;

    @Operation(summary = "일반 회원 가입", description = "사용자 이름, 닉네임, 비밀번호 입력", tags = {"사용자 관리 API"})
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserSignupRequestDto userSignupRequest) {
        UserAppResponseDto userAppResponseDto = userService.signup(userSignupRequest.toAppDto());
        return ResponseEntity.ok(UserResponseDto.from(userAppResponseDto));
    }

    @Operation(summary = "관리자 회원 가입", description = "사용자 이름, 닉네임, 비밀번호 입력", tags = {"사용자 관리 API"})
    @PostMapping("/signup/admin")
    public ResponseEntity<UserResponseDto> signupAdmin(@Valid @RequestBody UserSignupRequestDto userSignupRequest) {
        UserAppResponseDto userAppResponseDto = userService.signupAdmin(userSignupRequest.toAppDto());
        return ResponseEntity.ok(UserResponseDto.from(userAppResponseDto));
    }

    @Operation(summary = "관리자 권한 부여", description = "{username}회원의 권한 관리자로 변경", tags = {"사용자 관리 API"})
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/admin/users/{username}/roles")
    public ResponseEntity<UserResponseDto> grantAdmin(
            @Parameter(description = "관리자 권한 부여받을 유저네임")
            @PathVariable("username") String username){
        UserAppResponseDto userAppResponseDto = userService.grantAdmin(username);
        return ResponseEntity.ok(UserResponseDto.from(userAppResponseDto));
    }

    @Operation(summary = "모든 유저 정보 조회", description = "모든 유저의 이름, 권한 조회", tags = {"사용자 관리 API"})
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserAppResponseDto> all = userService.findAll();
        List<UserResponseDto> collect = all.stream().map(UserResponseDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @DeleteMapping("/users")
    public ResponseEntity<ApiResponse> deleteAllUsers() {
        userService.deleteAll();
        return ResponseEntity.ok(new ApiResponse());
    }


}
