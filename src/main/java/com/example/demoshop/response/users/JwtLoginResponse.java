package com.example.demoshop.response.users;

import jakarta.servlet.http.Cookie;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtLoginResponse {

    private String access;
    private String refresh;
    private Cookie cookie;

    @Builder
    public JwtLoginResponse(String access, String refresh, Cookie cookie) {
        this.access = access;
        this.refresh = refresh;
        this.cookie = cookie;
    }

}
