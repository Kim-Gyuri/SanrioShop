package com.example.demoshop.response.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 고객의 찜 리스트 페이지에 쓰이는 아이템 정렬용
 */
@Getter
@Setter
@NoArgsConstructor
public class WishlistItemDto {

    private Long id; // itemId
    private Long wishItemId;
    private String nameKor;
    private int price;
    private String thumbnail;


    @QueryProjection
    public WishlistItemDto(Long id, Long wishItemId, String nameKor, int price, String thumbnail) {
        this.id = id;
        this.wishItemId = wishItemId;
        this.nameKor = nameKor;
        this.price = price;
        this.thumbnail = thumbnail;
    }
}
