package com.example.demoshop.domain.wishList;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishItem is a Querydsl query type for WishItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishItem extends EntityPathBase<WishItem> {

    private static final long serialVersionUID = -1154138883L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishItem wishItem = new QWishItem("wishItem");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.demoshop.domain.item.QItem item;

    public final QWishList wishList;

    public QWishItem(String variable) {
        this(WishItem.class, forVariable(variable), INITS);
    }

    public QWishItem(Path<? extends WishItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishItem(PathMetadata metadata, PathInits inits) {
        this(WishItem.class, metadata, inits);
    }

    public QWishItem(Class<? extends WishItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new com.example.demoshop.domain.item.QItem(forProperty("item"), inits.get("item")) : null;
        this.wishList = inits.isInitialized("wishList") ? new QWishList(forProperty("wishList"), inits.get("wishList")) : null;
    }

}

