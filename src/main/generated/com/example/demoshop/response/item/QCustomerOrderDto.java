package com.example.demoshop.response.item;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.demoshop.response.item.QCustomerOrderDto is a Querydsl Projection type for CustomerOrderDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCustomerOrderDto extends ConstructorExpression<CustomerOrderDto> {

    private static final long serialVersionUID = -933390431L;

    public QCustomerOrderDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<Long> saleItemId, com.querydsl.core.types.Expression<String> nameKor, com.querydsl.core.types.Expression<Integer> price, com.querydsl.core.types.Expression<String> thumbnail) {
        super(CustomerOrderDto.class, new Class<?>[]{long.class, long.class, String.class, int.class, String.class}, id, saleItemId, nameKor, price, thumbnail);
    }

}

