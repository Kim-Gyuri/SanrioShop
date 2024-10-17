package com.example.demoshop.request.sale;

import com.example.demoshop.domain.transaction.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNotificationRequest {

    private String itemImg; // 상품 썸네일
    private String itemName; // 상품명
    private String buyerNick; // 구매자 닉네임
    public String message; // 알림 메시지

    private String userEmail; // 어떤 유저가 받는 알림인지.

    @Builder
    public CreateNotificationRequest(String itemImg, String itemName, String buyerNick, String message, String userEmail) {
        this.itemImg = itemImg;
        this.itemName = itemName;
        this.buyerNick = buyerNick;
        this.message = message;
        this.userEmail = userEmail;
    }


    public Notification toEntity() {
        return Notification.builder()
                .itemImg(itemImg)
                .itemName(itemName)
                .buyerNick(buyerNick)
                .message(message)
                .userEmail(userEmail)
                .build();
    }
}
