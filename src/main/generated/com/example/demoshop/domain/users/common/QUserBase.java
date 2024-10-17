package com.example.demoshop.domain.users.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserBase is a Querydsl query type for UserBase
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserBase extends EntityPathBase<UserBase> {

    private static final long serialVersionUID = 1104803763L;

    public static final QUserBase userBase = new QUserBase("userBase");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final EnumPath<UserLevel> userLevel = createEnum("userLevel", UserLevel.class);

    public QUserBase(String variable) {
        super(UserBase.class, forVariable(variable));
    }

    public QUserBase(Path<? extends UserBase> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserBase(PathMetadata metadata) {
        super(UserBase.class, metadata);
    }

}

