package com.example.demoshop.response.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 고객의 주문 조회 페이지 용도
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerOrderDto {

    private Long id; // itemId
    private Long saleItemId;
    private String nameKor;
    private int price;
    private String thumbnail;



    // 기존 생성자에 새로운 필드 추가
    @QueryProjection
    public CustomerOrderDto(Long id, Long saleItemId, String nameKor, int price, String thumbnail) {
        this.id = id;
        this.saleItemId = saleItemId;
        this.nameKor = nameKor;
        this.price = price;
        this.thumbnail = thumbnail;
    }
}
