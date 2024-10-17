package com.example.demoshop.response.item;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.demoshop.response.item.QWishlistItemDto is a Querydsl Projection type for WishlistItemDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWishlistItemDto extends ConstructorExpression<WishlistItemDto> {

    private static final long serialVersionUID = -955857899L;

    public QWishlistItemDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<Long> wishItemId, com.querydsl.core.types.Expression<String> nameKor, com.querydsl.core.types.Expression<Integer> price, com.querydsl.core.types.Expression<String> thumbnail) {
        super(WishlistItemDto.class, new Class<?>[]{long.class, long.class, String.class, int.class, String.class}, id, wishItemId, nameKor, price, thumbnail);
    }

}

