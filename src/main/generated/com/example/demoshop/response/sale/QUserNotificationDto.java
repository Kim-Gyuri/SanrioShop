package com.example.demoshop.response.sale;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.demoshop.response.sale.QUserNotificationDto is a Querydsl Projection type for UserNotificationDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QUserNotificationDto extends ConstructorExpression<UserNotificationDto> {

    private static final long serialVersionUID = 1154770347L;

    public QUserNotificationDto(com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> email) {
        super(UserNotificationDto.class, new Class<?>[]{String.class, String.class}, nickname, email);
    }

}

