package com.example.demoshop.response.sale;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 공지를 받을 사용자 정보를 담는 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserNotificationDto {

    private String nickname;
    private String email;

    @QueryProjection
    public UserNotificationDto(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
