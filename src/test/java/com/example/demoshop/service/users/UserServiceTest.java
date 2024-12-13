package com.example.demoshop.service.users;

import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.exception.users.DuplicateEmailException;
import com.example.demoshop.exception.users.DuplicateNicknameException;
import com.example.demoshop.exception.users.UserNotFoundException;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.request.users.SignupRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    UserService userService;


/*
    @AfterEach
    void cleanAfter() {
        userRepository.deleteAll();
    }


 */
    @Test
    @DisplayName("회원가입 - 성공 케이스")
    public void signup_success() {

        // given
        SignupRequest signupRequest = getSignupRequest("kiki");

        // When
        userService.signup(signupRequest);


        // then
        User user = userRepository.findByEmail("kiki1234@gmail.com")
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        assertNotNull(user);
        assertEquals("kiki1234@gmail.com", user.getEmail());
        assertEquals("kiki", user.getNickname());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    public void signup_fail_duplicate_email() {

        // given
        getUser("kiki");

        SignupRequest signupRequest = getDuplicateEmailSignupRequest();

        // when & then
        assertThrows(DuplicateEmailException.class, () -> userService.signup(signupRequest));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 닉네임")
    public void signup_fail_duplicate_nick() {

        // given
        getUser("jiji");
        SignupRequest signupRequest = getDuplicateNickSignupRequest();


        // when & then
        assertThrows(DuplicateNicknameException.class, () -> userService.signup(signupRequest));
    }

    @Test
    @DisplayName("닉네임 수정")
    void update_nick_success() {
        // given
        User user = getUser("jiji");

        // when
        userService.updateNickname(user, "purin");

        // then
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        assertEquals("purin", findUser.getNickname());
    }

    @Test
    @DisplayName("닉네임 수정 - 중복된 닉네임")
    void update_nick_fail_duplicate() {
        // given
        User me = getUser("jihyo");
        User other = getUser("nana");

        // when - then
        assertThrows(DuplicateNicknameException.class, () -> userService.updateNickname(me, "nana"));
    }

    @Test
    @DisplayName("프로필 수정")
    void update_profile_success() throws IOException {
        // given
        User user = getUser("hyerin");
        log.info("before img check = {}", user.getProfileImg());

        MockMultipartFile multipartFile = new MockMultipartFile("profileImg", "sanrio.png", "image/jpg", new byte[]{1, 2, 3, 4});

        // when
        userService.updateProfileImg(user, multipartFile);

        // then
        User findUser = userService.findById(user.getId());
        log.info("update img = {}", findUser.getProfileImg());
    }



    private User getUser(String name) {
        SignupRequest req = getSignupRequest(name);

        Long userId = userService.signup(req);

        return userService.findById(userId);
    }

    private static SignupRequest getSignupRequest(String name) {
        return SignupRequest.builder()
                .email(name + "1234@gmail.com")
                .password("1234")
                .nickname(name)
                .build();
    }

    private static SignupRequest getDuplicateEmailSignupRequest() {
        return SignupRequest.builder()
                .email("kiki1234@gmail.com")
                .password("1234")
                .nickname("karina")
                .build();
    }

    private static SignupRequest getDuplicateNickSignupRequest() {
        return SignupRequest.builder()
                .email("karina1234@gmail.com")
                .password("1234")
                .nickname("jiji")
                .build();
    }


}