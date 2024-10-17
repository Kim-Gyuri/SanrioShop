package com.example.demoshop.response.users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 프로필 수정 > 회원 정보(프로필, 닉네임) 불러오기
 */
@Getter
@Setter
@NoArgsConstructor
public class ProfileResponse {

    private String nickname; // 닉네임1
    private String profileImg; // 프로필 이미지 경로

    @Builder
    public ProfileResponse(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
