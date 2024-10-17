package com.example.demoshop.response.sale;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.demoshop.response.sale.QSaleItemResponse is a Querydsl Projection type for SaleItemResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSaleItemResponse extends ConstructorExpression<SaleItemResponse> {

    private static final long serialVersionUID = 677065113L;

    public QSaleItemResponse(com.querydsl.core.types.Expression<String> buyerEmail, com.querydsl.core.types.Expression<String> nameKor, com.querydsl.core.types.Expression<Integer> price) {
        super(SaleItemResponse.class, new Class<?>[]{String.class, String.class, int.class}, buyerEmail, nameKor, price);
    }

}

