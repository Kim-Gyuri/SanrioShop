package com.example.demoshop.response.item;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.demoshop.response.item.QSellerItemDto is a Querydsl Projection type for SellerItemDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSellerItemDto extends ConstructorExpression<SellerItemDto> {

    private static final long serialVersionUID = -1888862053L;

    public QSellerItemDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<Long> saleItemId, com.querydsl.core.types.Expression<String> nameKor, com.querydsl.core.types.Expression<Integer> price, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<Integer> likeCount, com.querydsl.core.types.Expression<String> thumbnail, com.querydsl.core.types.Expression<String> buyerEmail, com.querydsl.core.types.Expression<com.example.demoshop.domain.transaction.common.SaleStatus> status) {
        super(SellerItemDto.class, new Class<?>[]{long.class, long.class, String.class, int.class, java.time.LocalDateTime.class, int.class, String.class, String.class, com.example.demoshop.domain.transaction.common.SaleStatus.class}, id, saleItemId, nameKor, price, createAt, likeCount, thumbnail, buyerEmail, status);
    }

}

