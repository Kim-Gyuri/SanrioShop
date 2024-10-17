package com.example.demoshop.domain.users.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1233581282L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.example.demoshop.domain.users.common.QUserBase _super = new com.example.demoshop.domain.users.common.QUserBase(this);

    //inherited
    public final StringPath email = _super.email;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<com.example.demoshop.domain.item.Item, com.example.demoshop.domain.item.QItem> itemList = this.<com.example.demoshop.domain.item.Item, com.example.demoshop.domain.item.QItem>createList("itemList", com.example.demoshop.domain.item.Item.class, com.example.demoshop.domain.item.QItem.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    //inherited
    public final StringPath password = _super.password;

    public final StringPath profileImg = createString("profileImg");

    public final StringPath roles = createString("roles");

    //inherited
    public final EnumPath<com.example.demoshop.domain.users.common.UserLevel> userLevel = _super.userLevel;

    public final EnumPath<com.example.demoshop.domain.users.common.UserStatus> userStatus = createEnum("userStatus", com.example.demoshop.domain.users.common.UserStatus.class);

    public final com.example.demoshop.domain.wishList.QWishList wishList;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.wishList = inits.isInitialized("wishList") ? new com.example.demoshop.domain.wishList.QWishList(forProperty("wishList"), inits.get("wishList")) : null;
    }

}

