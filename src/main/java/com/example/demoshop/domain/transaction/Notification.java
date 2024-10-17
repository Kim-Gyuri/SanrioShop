package com.example.demoshop.domain.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


/**
 * 알림톡(알림 메시지) 관련 데이터를 저장하는 테이블
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String itemImg;
    private String itemName;

    private String buyerNick;
    public String message;

    private String userEmail; // 어떤 유저가 받는 알림인지.


    @Builder
    public Notification(String itemImg, String itemName, String buyerNick, String message, String userEmail) {
        this.itemImg = itemImg;
        this.itemName = itemName;
        this.buyerNick = buyerNick;
        this.message = message;
        this.userEmail = userEmail;
    }
}
