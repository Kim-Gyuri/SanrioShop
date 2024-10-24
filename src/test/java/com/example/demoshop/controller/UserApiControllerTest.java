package com.example.demoshop.controller;

import com.example.demoshop.domain.users.user.User;

import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.request.users.NicknameUpdate;
import com.example.demoshop.request.users.SignupRequest;
import com.example.demoshop.service.users.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserApiControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ResourceLoader loader;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @AfterEach
    void cleanAfter() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입 - 성공 케이스")
    void signup_success() throws Exception {
        // given
        SignupRequest signupRequest = getSignupRequest();

        // when
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/user/signUp")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
                )
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        User user = userRepository.findAll().get(0);
        assertEquals("none1234@gmail.com", user.getEmail());
    }


    @Test
    @DisplayName("회원가입 실패 테스트 - 중복된 이메일")
    void signUp_fail_duplicateEmail() throws Exception {

        // given
        userService.signup(getSignupRequest()); // 이미 가입된 회원 ; 이메일:none1234@gmail.com

        SignupRequest signupRequest = getDuplicateEmailSignupRequest();


        // when-then
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST, "/user/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isConflict()) // 중복된 이메일로 인해 409(CONFLICT) 상태 코드 반환
                .andExpect(content().string("중복된 이메일입니다.")) // 응답 메시지 확인
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 중복된 닉네임")
    void signUp_fail_duplicateNick() throws Exception {

        // given
        userService.signup(getSignupRequest()); // 이미 가입된 회원 ; 이메일:none1234@gmail.com

        SignupRequest signupRequest = getDuplicateNickSignupRequest();


        // when-then
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST, "/user/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isConflict()) // 중복된 닉네임으로 인해 409(CONFLICT) 상태 코드 반환
                .andExpect(content().string("중복된 닉네임입니다.")) // 응답 메시지 확인
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임 수정")
    void update_nick_success() throws Exception {
        // given
        User user = getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        NicknameUpdate nickRequest = getNickRequest();

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, "/user/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nickRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        User findUser = userRepository.findAll().get(0);
        assertEquals("cocoa", findUser.getNickname());
    }


    @Test
    @DisplayName("프로필 수정")
    void update_userProfile_success() throws Exception {

        // given
        User user = getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        MockMultipartFile profileFile = getProfileFile();

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, "/user/profile")
                        .file(profileFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        User findUser = userRepository.findAll().get(0);
        log.info("user img = {}", findUser.getProfileImg());
    }


    private MockMultipartFile getProfileFile() throws IOException {
        Resource res = loader.getResource("classpath:/static/images/pochaco.png");

        return new MockMultipartFile("file", "pochaco.png", "multipart/form-data", res.getInputStream());
    }



    private User getUser() {
        SignupRequest req = getSignupRequest();

        Long userId = userService.signup(req);

        return userService.findById(userId);
    }

    private NicknameUpdate getNickRequest() {
        return NicknameUpdate.builder()
                .nickname("cocoa")
                .build();
    }


    private static SignupRequest getSignupRequest() {
        return SignupRequest.builder()
                .email("none1234@gmail.com")
                .password("1234")
                .nickname("none")
                .build();
    }


    private static SignupRequest getDuplicateEmailSignupRequest() {
        return SignupRequest.builder()
                .email("none1234@gmail.com")
                .password("1234")
                .nickname("karina")
                .build();
    }

    private static SignupRequest getDuplicateNickSignupRequest() {
        return SignupRequest.builder()
                .email("karina1234@gmail.com")
                .password("1234")
                .nickname("none")
                .build();
    }




}