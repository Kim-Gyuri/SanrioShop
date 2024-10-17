package com.example.demoshop.request.users;

import com.example.demoshop.domain.users.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

    private String nickname;

    private String email;

    private String password;

    @Builder
    public SignupRequest(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public User ofEntity() {
        return User.builder()
                .email(this.getEmail())
                .password(this.getPassword())
                .nickname(this.getNickname())
                .build();
    }

   public void encodePwd(BCryptPasswordEncoder encoder) {
        String encodePwd = encoder.encode(password);
        this.password = encodePwd;
    }

}
