package com.example.demoshop.request.users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NicknameUpdate {

    String nickname;

    @Builder
    public NicknameUpdate(String nickname) {
        this.nickname = nickname;
    }
}
