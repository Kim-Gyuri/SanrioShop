package com.example.demoshop.controller;

import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.request.users.LoginRequest;
import com.example.demoshop.request.users.NicknameUpdate;
import com.example.demoshop.request.users.SignupRequest;
import com.example.demoshop.response.users.ProfileResponse;
import com.example.demoshop.service.users.JwtLoginService;
import com.example.demoshop.service.users.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.demoshop.utils.constants.ResponseConstants.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final JwtLoginService loginService;

    @GetMapping("/user-email/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkNicknameDuplicate(email));
    }

    @GetMapping("/user-nickname/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplicate(nickname));
    }

    @PostMapping("/user/signUp")
    public ResponseEntity<Void> signUp(@RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);

        return CREATED;
    }

    // 로그인
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        loginService.login(loginRequest, response);

    }

    //  프로필 수정 페이지 > 현재 로그인된 회원의 프로필 정보 불러오기
    @GetMapping("/user")
    public ProfileResponse currentUser(@AuthenticationPrincipal User user) {
        return userService.currentUserProfile(user);
    }

    @PatchMapping("/user/nickname")
    public void updateNickname(@AuthenticationPrincipal User user, @RequestBody NicknameUpdate nicknameUpdate) {
        userService.updateNickname(user, nicknameUpdate.getNickname());
    }

    @PatchMapping(value = "/user/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateProfile(@AuthenticationPrincipal User user, @RequestParam(name = "file") MultipartFile file) throws IOException {
        userService.updateProfileImg(user, file);
    }


}
