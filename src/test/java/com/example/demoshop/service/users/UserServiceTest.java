package com.example.demoshop.service.users;

import com.example.demoshop.domain.users.user.User;
import com.example.demoshop.exception.users.UserNotFoundException;
import com.example.demoshop.repository.users.UserRepository;
import com.example.demoshop.request.users.SignupRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    UserService userService;

    @Test
    public void testUserSignupAndFind() {
        // Given
        SignupRequest dto = SignupRequest.builder()
                .email("lullu1234@gmail.com")
                .password("12999034")
                .nickname("lullu")
                .build();

        // When
        userService.signup(dto);

        // Then
        User user = userRepository.findByEmail("lullu1234@gmail.com")
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));
        assertNotNull(user);
        assertEquals("lullu1234@gmail.com", user.getEmail());
        assertEquals("lullu", user.getNickname());

        userRepository.deleteAll();
    }
}