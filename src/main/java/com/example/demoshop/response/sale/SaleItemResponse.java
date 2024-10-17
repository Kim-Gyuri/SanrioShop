package com.example.demoshop.response.sale;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleItemResponse {

    private String buyerEmail;
    private String nameKor;
    private int price;

    @QueryProjection
    public SaleItemResponse(String buyerEmail, String nameKor, int price) {
        this.buyerEmail = buyerEmail;
        this.nameKor = nameKor;
        this.price = price;
    }
}
