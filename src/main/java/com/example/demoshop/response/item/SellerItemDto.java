package com.example.demoshop.response.item;

import com.example.demoshop.domain.transaction.common.SaleStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 판매자 페이지에 필요한 판매상품에 대한 아이템 정렬용
 */
@Getter
@Setter
@NoArgsConstructor
public class SellerItemDto {

    private Long id; // item pk
    private Long saleItemId; // saleItem pk
    private String nameKor;
    private int price;
    private LocalDateTime createAt; // item upload time
    private int likeCount;
    private String thumbnail;
    private String buyerEmail;
    private SaleStatus status; // 거래 상태


    // 기존 생성자에 새로운 필드 추가
    @QueryProjection
    public SellerItemDto(Long id, Long saleItemId, String nameKor, int price, LocalDateTime createAt, int likeCount, String thumbnail, String buyerEmail, SaleStatus status) {

        this.id = id;
        this.saleItemId = saleItemId;
        this.nameKor = nameKor;
        this.price = price;
        this.createAt = createAt;
        this.likeCount = likeCount;
        this.thumbnail = thumbnail;
        this.buyerEmail = buyerEmail;
        this.status = status;
    }
}
