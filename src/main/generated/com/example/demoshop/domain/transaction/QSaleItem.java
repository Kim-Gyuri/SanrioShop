package com.example.demoshop.domain.transaction;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSaleItem is a Querydsl query type for SaleItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSaleItem extends EntityPathBase<SaleItem> {

    private static final long serialVersionUID = -1954740144L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSaleItem saleItem = new QSaleItem("saleItem");

    public final com.example.demoshop.domain.users.user.QUser buyer;

    public final DateTimePath<java.time.LocalDateTime> created_date = createDateTime("created_date", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.demoshop.domain.item.QItem item;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final com.example.demoshop.domain.users.user.QUser seller;

    public final EnumPath<com.example.demoshop.domain.transaction.common.SaleStatus> status = createEnum("status", com.example.demoshop.domain.transaction.common.SaleStatus.class);

    public QSaleItem(String variable) {
        this(SaleItem.class, forVariable(variable), INITS);
    }

    public QSaleItem(Path<? extends SaleItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSaleItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSaleItem(PathMetadata metadata, PathInits inits) {
        this(SaleItem.class, metadata, inits);
    }

    public QSaleItem(Class<? extends SaleItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new com.example.demoshop.domain.users.user.QUser(forProperty("buyer"), inits.get("buyer")) : null;
        this.item = inits.isInitialized("item") ? new com.example.demoshop.domain.item.QItem(forProperty("item"), inits.get("item")) : null;
        this.seller = inits.isInitialized("seller") ? new com.example.demoshop.domain.users.user.QUser(forProperty("seller"), inits.get("seller")) : null;
    }

}

