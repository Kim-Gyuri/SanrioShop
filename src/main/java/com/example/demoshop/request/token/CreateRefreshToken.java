package com.example.demoshop.request.token;

import com.example.demoshop.domain.token.RefreshToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CreateRefreshToken {

    private String username;
    private String refresh;
    private long expiredMs;

    @Builder
    public CreateRefreshToken(String username, String refresh, long expiredMs) {
        this.username = username;
        this.refresh = refresh;
        this.expiredMs = expiredMs;
    }

    public RefreshToken ofEntity() {
        return RefreshToken.builder()
                .username(username)
                .refresh(refresh)
                .expiration(getDate().toString())
                .build();
    }


    private Date getDate() {
        return new Date(System.currentTimeMillis() + expiredMs);
    }

}
