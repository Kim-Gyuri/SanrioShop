package com.example.demoshop.domain.transaction;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = 1716011585L;

    public static final QNotification notification = new QNotification("notification");

    public final StringPath buyerNick = createString("buyerNick");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemImg = createString("itemImg");

    public final StringPath itemName = createString("itemName");

    public final StringPath message = createString("message");

    public final StringPath userEmail = createString("userEmail");

    public QNotification(String variable) {
        super(Notification.class, forVariable(variable));
    }

    public QNotification(Path<? extends Notification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotification(PathMetadata metadata) {
        super(Notification.class, metadata);
    }

}

